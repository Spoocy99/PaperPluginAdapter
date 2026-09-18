package dev.spoocy.adapter.dependencies;

import dev.spoocy.adapter.dependencies.service.DefaultLoadedDependency;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

class DependenciesTest {

    interface ServiceA {}
    interface ServiceB {}
    interface ServiceC {}
    interface ServiceD {}
    interface ParentService {}
    static class ChildService implements ParentService {}

    static class TestNode implements DependencyNode {
        private final String name;
        private final Set<Class<?>> requires;
        private final Set<Class<?>> provides;

        TestNode(String name, Set<Class<?>> requires, Set<Class<?>> provides) {
            this.name = name;
            this.requires = requires != null ? Set.copyOf(requires) : Set.of();
            this.provides = provides != null ? Set.copyOf(provides) : Set.of();
        }

        static TestNode of(String name, Set<Class<?>> requires, Set<Class<?>> provides) {
            return new TestNode(name, requires, provides);
        }

        static TestNode provider(String name, Class<?>... provides) {
            return new TestNode(name, Set.of(), Set.of(provides));
        }

        static TestNode consumer(String name, Class<?>... requires) {
            return new TestNode(name, Set.of(requires), Set.of());
        }

        static TestNode transformer(String name, Set<Class<?>> requires, Class<?>... provides) {
            return new TestNode(name, requires, Set.of(provides));
        }

        @Override
        public @NotNull Set<Class<?>> requires() {
            return requires;
        }

        @Override
        public @NotNull Set<Class<?>> provides() {
            return provides;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Test
    @DisplayName("Private constructor throws UnsupportedOperationException")
    void testConstructorThrows() throws NoSuchMethodException {
        Constructor<Dependencies> constructor = Dependencies.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException exception = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );
        assertInstanceOf(UnsupportedOperationException.class, exception.getCause());
        assertEquals("Utility class", exception.getCause().getMessage());
    }

    @Nested
    @DisplayName("topologicalOrder tests")
    class TopologicalOrderTests {

        @Test
        @DisplayName("Empty nodes returns empty list")
        void testEmptyNodes() {
            List<TestNode> result = Dependencies.topologicalOrder(List.of(), List.of());
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Single node with no requirements")
        void testSingleNodeNoRequirements() {
            TestNode node = TestNode.provider("NodeA", ServiceA.class);
            List<TestNode> result = Dependencies.topologicalOrder(List.of(node), List.of());

            assertEquals(1, result.size());
            assertEquals(node, result.get(0));
        }

        @Test
        @DisplayName("Multiple independent nodes with no requirements")
        void testMultipleIndependentNodes() {
            TestNode nodeA = TestNode.provider("NodeA", ServiceA.class);
            TestNode nodeB = TestNode.provider("NodeB", ServiceB.class);
            TestNode nodeC = TestNode.provider("NodeC", ServiceC.class);

            List<TestNode> result = Dependencies.topologicalOrder(List.of(nodeA, nodeB, nodeC), List.of());

            assertEquals(3, result.size());
            assertTrue(result.containsAll(List.of(nodeA, nodeB, nodeC)));
        }

        @Test
        @DisplayName("Linear dependency chain (A -> B -> C)")
        void testLinearDependencyChain() {
            TestNode nodeA = TestNode.provider("NodeA", ServiceA.class);
            TestNode nodeB = TestNode.transformer("NodeB", Set.of(ServiceA.class), ServiceB.class);
            TestNode nodeC = TestNode.consumer("NodeC", ServiceB.class);

            // Pass in reverse order to ensure sorting works correctly
            List<TestNode> result = Dependencies.topologicalOrder(List.of(nodeC, nodeB, nodeA), List.of());

            assertEquals(3, result.size());
            assertEquals(nodeA, result.get(0));
            assertEquals(nodeB, result.get(1));
            assertEquals(nodeC, result.get(2));
        }

        @Test
        @DisplayName("Diamond dependency pattern (A -> B, A -> C, (B, C) -> D)")
        void testDiamondDependency() {
            TestNode nodeA = TestNode.provider("NodeA", ServiceA.class);
            TestNode nodeB = TestNode.transformer("NodeB", Set.of(ServiceA.class), ServiceB.class);
            TestNode nodeC = TestNode.transformer("NodeC", Set.of(ServiceA.class), ServiceC.class);
            TestNode nodeD = TestNode.of("NodeD", Set.of(ServiceB.class, ServiceC.class), Set.of(ServiceD.class));

            List<TestNode> result = Dependencies.topologicalOrder(List.of(nodeD, nodeC, nodeB, nodeA), List.of());

            assertEquals(4, result.size());
            assertEquals(nodeA, result.get(0));
            assertEquals(nodeD, result.get(3));

            int indexB = result.indexOf(nodeB);
            int indexC = result.indexOf(nodeC);
            assertTrue(indexB > 0 && indexB < 3);
            assertTrue(indexC > 0 && indexC < 3);
        }

        @Test
        @DisplayName("Requirement satisfied by pre-loaded dependency")
        void testPreLoadedDependency() {
            TestNode nodeA = TestNode.consumer("NodeA", ServiceA.class);
            Dependency<ServiceA> loadedDep = new DefaultLoadedDependency<>(
                    ServiceA.class,
                    new ServiceA() {}
            );

            List<TestNode> result = Dependencies.topologicalOrder(List.of(nodeA), List.of(loadedDep));

            assertEquals(1, result.size());
            assertEquals(nodeA, result.get(0));
        }

        @Test
        @DisplayName("Combined pre-loaded dependency and node dependency")
        void testCombinedPreLoadedAndNodeDependency() {
            TestNode nodeA = TestNode.provider("NodeA", ServiceA.class);
            TestNode nodeB = TestNode.of("NodeB", Set.of(ServiceA.class, ServiceB.class), Set.of());

            Dependency<ServiceB> loadedDep = new DefaultLoadedDependency<>(
                    ServiceB.class,
                    new ServiceB() {}
            );

            List<TestNode> result = Dependencies.topologicalOrder(List.of(nodeB, nodeA), List.of(loadedDep));

            assertEquals(2, result.size());
            assertEquals(nodeA, result.get(0));
            assertEquals(nodeB, result.get(1));
        }

        @Test
        @DisplayName("Requirement satisfied by subtype/polymorphism (ChildService provides ParentService requirement)")
        void testPolymorphicDependency() {
            TestNode provider = TestNode.provider("ChildProvider", ChildService.class);
            TestNode consumer = TestNode.consumer("ParentConsumer", ParentService.class);

            List<TestNode> result = Dependencies.topologicalOrder(List.of(consumer, provider), List.of());

            assertEquals(2, result.size());
            assertEquals(provider, result.get(0));
            assertEquals(consumer, result.get(1));
        }

        @Test
        @DisplayName("Pre-loaded subtype satisfies requirement (ChildService in loaded satisfies ParentService requirement)")
        void testPreLoadedPolymorphicDependency() {
            TestNode consumer = TestNode.consumer("ParentConsumer", ParentService.class);
            Dependency<ChildService> loadedDep = new DefaultLoadedDependency<>(
                    ChildService.class,
                    new ChildService()
            );

            List<TestNode> result = Dependencies.topologicalOrder(List.of(consumer), List.of(loadedDep));

            assertEquals(1, result.size());
            assertEquals(consumer, result.get(0));
        }

        @Test
        @DisplayName("Single provider node providing multiple required types")
        void testSingleProviderMultipleRequirements() {
            TestNode provider = TestNode.of("Provider", Set.of(), Set.of(ServiceA.class, ServiceB.class));
            TestNode consumer = TestNode.of("Consumer", Set.of(ServiceA.class, ServiceB.class), Set.of());

            List<TestNode> result = Dependencies.topologicalOrder(List.of(consumer, provider), List.of());

            assertEquals(2, result.size());
            assertEquals(provider, result.get(0));
            assertEquals(consumer, result.get(1));
        }

        @Test
        @DisplayName("Missing dependency throws IllegalStateException with class name")
        void testMissingDependencyThrows() {
            TestNode consumer = TestNode.consumer("Consumer", ServiceA.class);

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> Dependencies.topologicalOrder(List.of(consumer), List.of())
            );

            assertTrue(exception.getMessage().contains("TestNode"));
            assertTrue(exception.getMessage().contains("ServiceA"));
        }

        @Test
        @DisplayName("Multiple missing dependencies listed in exception message")
        void testMultipleMissingDependenciesThrows() {
            TestNode consumer = TestNode.consumer("Consumer", ServiceA.class, ServiceB.class);

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> Dependencies.topologicalOrder(List.of(consumer), List.of())
            );

            assertTrue(exception.getMessage().contains("ServiceA"));
            assertTrue(exception.getMessage().contains("ServiceB"));
        }

        @Test
        @DisplayName("Direct cycle (A -> B -> A) throws IllegalStateException")
        void testDirectCycleThrows() {
            TestNode nodeA = TestNode.transformer("NodeA", Set.of(ServiceB.class), ServiceA.class);
            TestNode nodeB = TestNode.transformer("NodeB", Set.of(ServiceA.class), ServiceB.class);

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> Dependencies.topologicalOrder(List.of(nodeA, nodeB), List.of())
            );

            assertTrue(exception.getMessage().contains("Cyclic or unresolvable"));
        }

        @Test
        @DisplayName("Indirect cycle (A -> B -> C -> A) throws IllegalStateException")
        void testIndirectCycleThrows() {
            TestNode nodeA = TestNode.transformer("NodeA", Set.of(ServiceC.class), ServiceA.class);
            TestNode nodeB = TestNode.transformer("NodeB", Set.of(ServiceA.class), ServiceB.class);
            TestNode nodeC = TestNode.transformer("NodeC", Set.of(ServiceB.class), ServiceC.class);

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> Dependencies.topologicalOrder(List.of(nodeA, nodeB, nodeC), List.of())
            );

            assertTrue(exception.getMessage().contains("Cyclic or unresolvable"));
        }

        @Test
        @DisplayName("Self dependency throws IllegalStateException")
        void testSelfDependencyThrows() {
            TestNode nodeA = TestNode.transformer("NodeA", Set.of(ServiceA.class), ServiceA.class);

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> Dependencies.topologicalOrder(List.of(nodeA), List.of())
            );

            assertTrue(exception.getMessage().contains("missing dependencies"));
        }

        @Test
        @DisplayName("Partial cycle with resolvable independent node throws IllegalStateException for cycle")
        void testPartialCycleThrows() {
            TestNode independent = TestNode.provider("Independent", ServiceC.class);
            TestNode nodeA = TestNode.transformer("NodeA", Set.of(ServiceB.class), ServiceA.class);
            TestNode nodeB = TestNode.transformer("NodeB", Set.of(ServiceA.class), ServiceB.class);

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> Dependencies.topologicalOrder(List.of(independent, nodeA, nodeB), List.of())
            );

            assertTrue(exception.getMessage().contains("Cyclic or unresolvable"));
        }
    }

    @Nested
    @DisplayName("buildDependencyGraph tests")
    class BuildDependencyGraphTests {

        @Test
        @DisplayName("Graph inDegree and dependents are constructed accurately")
        void testBuildGraphDetails() {
            TestNode nodeA = TestNode.provider("NodeA", ServiceA.class);
            TestNode nodeB = TestNode.transformer("NodeB", Set.of(ServiceA.class), ServiceB.class);
            TestNode nodeC = TestNode.consumer("NodeC", ServiceA.class, ServiceB.class);

            DependencyGraph<TestNode> graph = Dependencies.buildDependencyGraph(
                    List.of(nodeA, nodeB, nodeC),
                    List.of()
            );

            Map<TestNode, Integer> inDegree = graph.inDegree();
            Map<TestNode, Set<TestNode>> dependents = graph.dependents();

            assertEquals(0, inDegree.get(nodeA));
            assertEquals(1, inDegree.get(nodeB));
            assertEquals(2, inDegree.get(nodeC));

            assertEquals(Set.of(nodeB, nodeC), dependents.get(nodeA));
            assertEquals(Set.of(nodeC), dependents.get(nodeB));
            assertEquals(Set.of(), dependents.get(nodeC));

            assertEquals(List.of(nodeA), graph.getNodesWithZeroInDegree());
            assertEquals(2, graph.getNodesWithPositiveInDegree().size());
            assertTrue(graph.getNodesWithPositiveInDegree().containsAll(List.of(nodeB, nodeC)));
        }

        @Test
        @DisplayName("decrementAndGetInDegree decrements correctly and validates bounds")
        void testDecrementAndGetInDegree() {
            TestNode nodeA = TestNode.provider("NodeA", ServiceA.class);
            TestNode nodeB = TestNode.consumer("NodeB", ServiceA.class);

            DependencyGraph<TestNode> graph = Dependencies.buildDependencyGraph(
                    List.of(nodeA, nodeB),
                    List.of()
            );

            assertEquals(1, graph.inDegree().get(nodeB));
            int newDegree = graph.decrementAndGetInDegree(nodeB);
            assertEquals(0, newDegree);
            assertEquals(0, graph.inDegree().get(nodeB));

            assertThrows(IllegalStateException.class, () -> graph.decrementAndGetInDegree(nodeB));
            assertThrows(IllegalStateException.class, () -> graph.decrementAndGetInDegree(nodeA));
        }
    }
}
