package main.java.view;

import main.java.util.SoundManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ComponentAnimator {


    private final java.util.Map<JComponent, Timer> active = new java.util.WeakHashMap<>();

    private void replaceTimer(JComponent c, Timer next) {
        Timer prev = active.put(c, next);
        if (prev != null) {
            prev.stop();
            // reset to stable base if we stored it
            Point base = (Point) c.getClientProperty("shake.base");
            if (base != null) c.setLocation(base);
        }
    }


    public void shake(JComponent c) {
        // store a stable base once
        Point base = (Point) c.getClientProperty("shake.base");
        if (base == null) {
            base = c.getLocation();
            c.putClientProperty("shake.base", new Point(base));
        } else {
            base = new Point(base);
        }

        int frames = 10;
        int[] i = {0};

        Point finalBase = base;
        Timer t = new Timer(15, e -> {
            i[0]++;
            int dx = (i[0] % 2 == 0) ? 6 : -6;
            c.setLocation(finalBase.x + dx, finalBase.y);

            if (i[0] >= frames) {
                c.setLocation(finalBase);
                ((Timer) e.getSource()).stop();
            }
        });

        // reset before starting new one
        c.setLocation(base);
        replaceTimer(c, t);
        t.start();
    }


    public void pulseBorder(JComponent c, int maxGrowPx) {
        Border originalBorder = c.getBorder();
        Insets baseInsets = (originalBorder instanceof EmptyBorder eb)
                ? eb.getBorderInsets()
                : new Insets(5, 10, 5, 10);

        int frames = 16;
        int[] i = {0};

        Timer t = new Timer(15, e -> {
            i[0]++;
            int half = frames / 2;
            int k = (i[0] <= half) ? i[0] : (frames - i[0]); // up then down

            int grow = (int) Math.round((k / (double) half) * maxGrowPx);

            c.setBorder(BorderFactory.createEmptyBorder(
                    Math.max(0, baseInsets.top - grow / 2),
                    Math.max(0, baseInsets.left - grow),
                    Math.max(0, baseInsets.bottom - grow / 2),
                    Math.max(0, baseInsets.right - grow)
            ));
            c.revalidate();
            c.repaint();

            if (i[0] >= frames) {
                c.setBorder(originalBorder);
                ((Timer) e.getSource()).stop();
            }
        });

        replaceTimer(c, t);
        t.start();
    }

    public void flashForeground(JComponent label, Color flashColor, Color baseColor) {
        int frames = 18;
        int[] i = {0};

        Timer t = new Timer(20, e -> {
            i[0]++;
            float a = i[0] / (float) frames; // 0..1

            int r = (int) (flashColor.getRed()   * (1 - a) + baseColor.getRed()   * a);
            int g = (int) (flashColor.getGreen() * (1 - a) + baseColor.getGreen() * a);
            int b = (int) (flashColor.getBlue()  * (1 - a) + baseColor.getBlue()  * a);

            label.setForeground(new Color(r, g, b));

            if (i[0] >= frames) ((Timer) e.getSource()).stop();
        });

        replaceTimer(label, t);
        t.start();
    }

    public void flashBackground(JComponent c, Color flashColor, Color base) {
        c.setOpaque(true);

        int frames = 18;
        int[] i = {0};

        Timer t = new Timer(20, e -> {
            i[0]++;
            float a = i[0] / (float) frames;

            int r = (int) (flashColor.getRed()   * (1 - a) + base.getRed()   * a);
            int g = (int) (flashColor.getGreen() * (1 - a) + base.getGreen() * a);
            int b = (int) (flashColor.getBlue()  * (1 - a) + base.getBlue()  * a);

            c.setBackground(new Color(r, g, b));

            if (i[0] >= frames) {
                c.setBackground(base);
                c.setOpaque(false);
                ((Timer) e.getSource()).stop();
            }
        });

        c.setBackground(base);

        replaceTimer(c, t);
        t.start();
    }


    public void floatingNumber(JComponent target, String text, Color color, boolean isUp) {
        JRootPane rootPane = SwingUtilities.getRootPane(target);
        if (rootPane == null) return;

        OutlinedLabel floatLabel = new OutlinedLabel(text, Color.BLACK, 2.5f);
        floatLabel.setFont(FontsInUse.PIXEL.getSize(30f));
        floatLabel.setForeground(color);

        Point screenPos = target.getLocationOnScreen();
        Point rootPos = rootPane.getLocationOnScreen();
        int x = screenPos.x - rootPos.x + (target.getWidth() / 2) - 15;
        int y = screenPos.y - rootPos.y;

        floatLabel.setBounds(x, y, 100, 30);

        JLayeredPane layeredPane = rootPane.getLayeredPane();
        layeredPane.add(floatLabel, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();

        //animation timer
        int distance = 50;
        int step;
        if(!isUp) {
            step = 2;
        } else {
            step = -2;
        }
        Timer timer = new Timer(40, null);
        timer.addActionListener(e -> {
            Point p = floatLabel.getLocation();
            floatLabel.setLocation(p.x, p.y + step);

            //define when to stop based on the direction
            boolean finished;
            if (isUp) {
                finished = (p.y < y - distance);
            }
            else {
                finished = (p.y > y + distance);
            }

            if (finished) {
                layeredPane.remove(floatLabel);
                layeredPane.repaint();
                timer.stop();
            }

        });
        timer.start();
    }

    public void pulse(JComponent c) {
        Insets original = c.getBorder() instanceof EmptyBorder eb ? eb.getBorderInsets() : new Insets(5, 20, 5, 20);
        int max = 8;
        int[] t = {0};

        Timer timer = new Timer(15, e -> {
            t[0] += 1;
            int k = (t[0] <= max) ? t[0] : (2 * max - t[0]); // up then down
            c.setBorder(BorderFactory.createEmptyBorder(
                    original.top - k/2,
                    original.left - k,
                    original.bottom - k/2,
                    original.right - k
            ));
            c.revalidate();
            c.repaint();

            if (t[0] >= 2 * max) {
                c.setBorder(BorderFactory.createEmptyBorder(original.top, original.left, original.bottom, original.right));
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    public Timer randomNumber(JLabel label, int target) {
        final int durationMs = 700;
        final int tickMs = 25;
        final int steps = durationMs / tickMs;

        final int[] step = {0};

        Timer t = new Timer(tickMs, e -> {
            step[0]++;

            int rand = (int) (Math.random() * 100);
            label.setText(String.valueOf(rand));
            label.setForeground(rand >= target ? ColorsInUse.FEEDBACK_GOOD_COLOR.get() : ColorsInUse.FEEDBACK_BAD_COLOR.get());

            // 🔊 play on every tick
            SoundManager.getInstance()
                    .playOnce(SoundManager.SoundId.SELECTION);

            if (step[0] >= steps) {
                ((Timer) e.getSource()).stop();
                label.setText(String.valueOf(target));
            }
        });

        t.start();
        return t;
    }

    public void closeCountdown(JButton button, int target, int startValue) {
        final int durationMs = startValue*1000;
        final int tickMs = 1000;
        final int steps = durationMs / tickMs;

        final int[] step = {0};

        button.setText("Close (" + String.valueOf(startValue) +")");

        Timer t = new Timer(tickMs, e -> {
            step[0]++;

            int currentValue = (startValue - (step[0] * (startValue - target) / steps));

            button.setText("Close (" + String.valueOf(currentValue) +")");

            // play on every tick
            //SoundManager.getInstance().playOnce(SoundManager.SoundId.SELECTION);

            if (step[0] >= steps) {
                ((Timer) e.getSource()).stop();
                button.setText("Close (" + String.valueOf(currentValue) +")");
            }
        });

        t.start();
    }

    public void stopAllAnimations() {
        active.values().forEach(Timer::stop);
    }

    public boolean isRunning() {
        return !active.isEmpty();
    }
}