package dev.spoocy.adapter.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * Utility class for serializing and deserializing Bukkit {@link ItemStack} objects to and from byte arrays.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class ItemSerializer {

    private ItemSerializer() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Serializes a single {@link ItemStack} into a byte array.
     *
     * @param item the {@link ItemStack} to serialize
     *
     * @return a non-null byte array containing the serialized item data
     *
     * @throws RuntimeException if an I/O error occurs during serialization
     */
    public static byte @NotNull [] serializeItemAsBytes(@NotNull ItemStack item) {
        try {
            return item.serializeAsBytes();
        } catch (NoSuchMethodError ignored) {
            // not available so use bukkit streams
        }

        try (
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)
        ) {

            dataOutput.writeObject(item);
            dataOutput.flush();
            return outputStream.toByteArray();

        } catch (final IOException e) {
            throw new RuntimeException("Error while serializing item.", e);
        }

    }

    /**
     * Deserializes a single {@link ItemStack} from a byte array.
     *
     * @param bytes the byte array containing the serialized {@link ItemStack}
     *
     * @return the deserialized {@link ItemStack}
     *
     * @throws RuntimeException if an I/O or class not found error occurs during deserialization
     */
    @NotNull
    public static ItemStack deserializeItemFromBytes(byte @NotNull [] bytes) {
        try {
            return ItemStack.deserializeBytes(bytes);
        } catch (NoSuchMethodError ignored) {
            // not available so use bukkit streams
        }

        try (
                final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)
        ) {

            return (ItemStack) dataInput.readObject();

        } catch (final IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error while deserializing item.", e);
        }
    }

    /**
     * Serializes an array of {@link ItemStack}s into a single byte array.
     * <p>
     * Null elements in the array are preserved during serialization to maintain slot order.
     * </p>
     *
     * @param items the array of {@link ItemStack}s to serialize, which may contain null elements
     *
     * @return a non-null byte array containing the serialized items data
     *
     * @throws RuntimeException if an I/O error occurs during serialization
     */
    public static byte @NotNull [] serializeItemsAsBytes(@Nullable ItemStack @NotNull [] items) {
        return serializeItemsAsBytes(Arrays.asList(items));
    }

    /**
     * Serializes a collection of {@link ItemStack}s into a single byte array.
     * <p>
     * Null elements within the collection are preserved during serialization so that
     * slot positioning and order are maintained upon deserialization.
     * </p>
     *
     * @param items the collection of {@link ItemStack}s to serialize
     *
     * @return a non-null byte array containing the serialized items data
     *
     * @throws RuntimeException if an I/O error occurs during serialization
     */
    public static byte @NotNull [] serializeItemsAsBytes(@NotNull Collection<ItemStack> items) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            final DataOutput output = new DataOutputStream(outputStream);

            output.writeInt(items.size());

            for (final ItemStack item : items) {

                if (item == null) {
                    // Ensure the correct order by including empty/null items
                    output.writeInt(0);
                    continue;
                }

                final byte[] itemBytes = serializeItemAsBytes(item);
                output.writeInt(itemBytes.length);
                output.write(itemBytes);
            }

            return outputStream.toByteArray();

        } catch (IOException e) {

            throw new RuntimeException("Error while serializing items.", e);
        }
    }

    /**
     * Deserializes an array of {@link ItemStack}s from a byte array.
     * <p>
     * Empty or null slots recorded during serialization are returned as null elements in the array.
     * </p>
     *
     * @param bytes the byte array containing the serialized items
     *
     * @return an array of deserialized {@link ItemStack}s, where individual elements may be null
     *
     * @throws RuntimeException if an I/O error occurs during deserialization
     */
    public static @Nullable ItemStack @NotNull [] deserializeItemsFromBytes(final byte @NotNull [] bytes) {
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {

            final DataInputStream input = new DataInputStream(inputStream);
            final int count = input.readInt();
            final ItemStack[] items = new ItemStack[count];

            for (int i = 0; i < count; i++) {
                final int length = input.readInt();

                if (length == 0) {
                    // Empty item, keep entry as empty
                    items[i] = null;
                    continue;
                }

                final byte[] itemBytes = new byte[length];
                input.readFully(itemBytes);
                items[i] = deserializeItemFromBytes(itemBytes);
            }
            return items;

        } catch (IOException e) {
            throw new RuntimeException("Error while deserializing items.", e);
        }
    }

}
