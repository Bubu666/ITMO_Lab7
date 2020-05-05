package client.animation;

import java.util.HashMap;

public class WaitingForServerAnimation implements Runnable {
    private static int animationStage = 0;
    private boolean interrupted = true;

    private static final HashMap<Integer, String> wfs = new HashMap<>();

    static {
        wfs.put(0, "\r                      ");
        wfs.put(1, "\rW");
        wfs.put(2, "a");
        wfs.put(3, "i");
        wfs.put(4, "t");
        wfs.put(5, "i");
        wfs.put(6, "n");
        wfs.put(7, "g");
        wfs.put(8, " f");
        wfs.put(9, "o");
        wfs.put(10, "r");
        wfs.put(11, " s");
        wfs.put(12, "e");
        wfs.put(13, "r");
        wfs.put(14, "v");
        wfs.put(15, "e");
        wfs.put(16, "r");
        wfs.put(17, ".");
        wfs.put(18, ".");
        wfs.put(19, ".");
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void interrupt() {
        interrupted = true;
    }

    @Override
    public void run() {
        interrupted = false;

        try {
            System.out.print("\r                           ");
            while (!interrupted) {
                System.out.print(wfs.get(animationStage = (animationStage + 1) % 20));
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {}

        animationStage = 0;
    }
}
