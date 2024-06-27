package commands;

public class ShuffleCommand extends CommandInput {
    private int seed;

    public ShuffleCommand() {
        super();
        command = "shuffle";
    }

    public final int getSeed() {
        return seed;
    }
}
