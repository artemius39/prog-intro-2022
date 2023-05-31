import java.util.Arrays;

public class IntList {
    private int[] numbers = new int[1];
    private int size = 0;

    public void add(int number) {
        if (size >= numbers.length - 1) {
            numbers = Arrays.copyOf(numbers, numbers.length * 2);
        }
        numbers[size] = number;
        size++;
    }

    public int get(int i) {
        return numbers[i];
    }

    public int size() {
        return size;
    }
}
