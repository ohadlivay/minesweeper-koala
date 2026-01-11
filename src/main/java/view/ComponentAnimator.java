package main.java.view;

import main.java.util.SoundManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.LayerUI;
import java.awt.*;

public class ComponentAnimator {


    private final java.util.Map<JComponent, Timer> active = new java.util.WeakHashMap<>();

    private final java.util.Map<JComponent, SparkleOverlay> sparkleOverlays = new java.util.WeakHashMap<>();
    private final java.util.Map<JComponent, Timer> sparkleEmitTimers = new java.util.WeakHashMap<>();


    //blocks new animations if one is already running
    private boolean canAnimate(JComponent c, Timer next) {
        if (active.containsKey(c)) {
            return false;
        }
        active.put(c, next);
        return true;
    }


    public void shake(JComponent c) {
        Border stableBorder = (Border) c.getClientProperty("shake.stableBorder");
        if (stableBorder == null) {
            stableBorder = c.getBorder();
            c.putClientProperty("shake.stableBorder", stableBorder);
        }

        final Border finalStable = stableBorder;
        Insets base = (finalStable instanceof EmptyBorder eb) ? eb.getBorderInsets() : new Insets(0,0,0,0);

        int frames = 10;
        int[] i = {0};

        Timer t = new Timer(15, e -> {
            i[0]++;

            int offset = (i[0] % 2 == 0) ? 2 : -2;

            c.setBorder(BorderFactory.createEmptyBorder(
                    base.top, base.left + offset, base.bottom, base.right - offset
            ));

            if (i[0] >= frames) {
                c.setBorder(finalStable);
                active.remove(c);
                ((Timer) e.getSource()).stop();
            }
        });

        if (canAnimate(c, t)) {
            t.start();
        }
    }


    public void pulseBorder(JComponent c, int maxGrowPx) {
        Border stableBorder = (Border) c.getClientProperty("pulse.stableBorder");
        if (stableBorder == null) {
            stableBorder = c.getBorder();
            c.putClientProperty("pulse.stableBorder", stableBorder);
        }

        final Border finalStable = stableBorder;
        Insets baseInsets = (finalStable instanceof EmptyBorder eb)
                ? eb.getBorderInsets() : new Insets(5, 10, 5, 10);

        int frames = 16;
        int[] i = {0};

        Timer t = new Timer(15, e -> {
            i[0]++;
            int half = frames / 2;
            int k = (i[0] <= half) ? i[0] : (frames - i[0]);
            int grow = (int) Math.round((k / (double) half) * maxGrowPx);

            c.setBorder(BorderFactory.createEmptyBorder(
                    Math.max(0, baseInsets.top - grow / 2),
                    Math.max(0, baseInsets.left - grow),
                    Math.max(0, baseInsets.bottom - grow / 2),
                    Math.max(0, baseInsets.right - grow)
            ));

            if (i[0] >= frames) {
                c.setBorder(finalStable);
                active.remove(c);
                ((Timer) e.getSource()).stop();
            }
        });

        if (canAnimate(c, t)) t.start();

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

        if (canAnimate(label, t)) t.start();
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

        if (canAnimate(c, t)) t.start();
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

        button.setText("Close (" + startValue +")");

        Timer t = new Timer(tickMs, e -> {
            step[0]++;

            int currentValue = (startValue - (step[0] * (startValue - target) / steps));

            button.setText("Close (" + currentValue +")");

            // play on every tick
            //SoundManager.getInstance().playOnce(SoundManager.SoundId.SELECTION);

            if (step[0] >= steps) {
                ((Timer) e.getSource()).stop();
                button.setText("Close (" + currentValue +")");
            }
        });

        t.start();
    }

    //tile animations
    public JComponent withEffects(JComponent comp) {
        LayerUI<JComponent> ui = new LayerUI<>() {
            @Override
            public void paint(Graphics g, JComponent c) {
                // c is the JLayer itself
                if (!(c instanceof JLayer<?> layer)) return;

                Component viewComp = layer.getView();
                if (!(viewComp instanceof JComponent view)) return;

                // ✅ paint the wrapped component normally (prevents Synth casting issues)
                view.paint(g);

                // ===== Shine overlay (optional) =====
                Float phase = (Float) view.getClientProperty("shine.phase");
                if (phase == null) return;

                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int w = view.getWidth();
                    int h = view.getHeight();

                    // diagonal band x position goes from left to right across the component
                    float x = (phase * (w + h)) - h;

                    Polygon band = new Polygon();
                    band.addPoint((int) x, 0);
                    band.addPoint((int) (x + h * 0.6f), 0);
                    band.addPoint((int) (x + h), h);
                    band.addPoint((int) (x + h * 0.4f), h);

                    g2.setComposite(AlphaComposite.SrcOver.derive(0.28f));
                    g2.setPaint(new GradientPaint(
                            0, 0, new Color(255, 255, 255, 0),
                            w, h, new Color(255, 255, 255, 220)
                    ));
                    g2.fillPolygon(band);
                } finally {
                    g2.dispose();
                }
            }
        };

        return new JLayer<>(comp, ui);
    }

    public void shine(JComponent comp, boolean loop) {
        // normalize: if someone passes a JLayer by accident, animate the view
        if (comp instanceof JLayer<?> layer && layer.getView() instanceof JComponent v) {
            comp = v;
        }

        comp.putClientProperty("shine.phase", 0f);

        final int tickMs = 30;
        final float speed = 0.03f;

        JComponent finalComp = comp;
        Timer t = new Timer(tickMs, e -> {
            Float pObj = (Float) finalComp.getClientProperty("shine.phase");
            float p = (pObj == null) ? 0f : pObj;

            p += speed;

            if (p >= 1f) {
                if (loop) {
                    p = 0f;
                } else {
                    ((Timer) e.getSource()).stop();
                    finalComp.putClientProperty("shine.phase", null);
                }
            }

            finalComp.putClientProperty("shine.phase", p);
            finalComp.repaint();
        });

        if (canAnimate(comp, t)) t.start();
    }

    // Breath

    public void stop(JComponent c) {
        Timer prev = active.remove(c);
        if (prev != null && prev.isRunning()) prev.stop();

        Color base = (Color) c.getClientProperty("breath.baseColor");
        if (base != null) c.setBackground(base);

        c.putClientProperty("breath.baseColor", null);
        c.putClientProperty("breath.glowColor", null);
        c.putClientProperty("breath.t0", null);
        c.putClientProperty("shine.phase", null);
        c.repaint();
    }

    /**
     * Idle breathing pulse: smoothly blends component background between baseColor and glowColor.
     * periodMs ~ 900-1400 feels good.
     */
    public void breathe(JComponent c, Color glowColor, int periodMs, boolean on) {
        if (!on) {
            stop(c);
            return;
        }

        // if already running
        Timer prev = active.get(c);
        if (prev != null && prev.isRunning()) return;

        // capture base background once
        Color base = c.getBackground();
        c.putClientProperty("breath.baseColor", base);
        c.putClientProperty("breath.glowColor", glowColor);
        c.putClientProperty("breath.t0", System.currentTimeMillis());

        // smoother than 60fps, cheap enough
        final int tickMs = 30;

        Timer t = new Timer(tickMs, e -> {
            Long t0 = (Long) c.getClientProperty("breath.t0");
            Color b = (Color) c.getClientProperty("breath.baseColor");
            Color g = (Color) c.getClientProperty("breath.glowColor");
            if (t0 == null || b == null || g == null) {
                ((Timer) e.getSource()).stop();
                return;
            }

            long dt = System.currentTimeMillis() - t0;

            // 0..1..0 with sine
            double phase = (dt % periodMs) / (double) periodMs;         // 0..1
            double s = (Math.sin(phase * Math.PI * 2) + 1) / 2.0;       // 0..1

            // keep it subtle (adjust 0.35 if you want stronger glow)
            double intensity = 0.85 * s;

            int r = (int) Math.round(b.getRed()   * (1 - intensity) + g.getRed()   * intensity);
            int gg = (int) Math.round(b.getGreen() * (1 - intensity) + g.getGreen() * intensity);
            int bb = (int) Math.round(b.getBlue()  * (1 - intensity) + g.getBlue()  * intensity);

            c.setBackground(new Color(r, gg, bb));
            c.repaint();
        });

        if (canAnimate(c, t)) t.start();
    }


    public void sparkle(JComponent target, boolean on) {
        if (!on) {
            stopSparkle(target);
            return;
        }

        // already on?
        if (sparkleEmitTimers.containsKey(target)) return;

        JRootPane root = SwingUtilities.getRootPane(target);
        if (root == null) return;

        SparkleOverlay overlay = new SparkleOverlay(target, root);
        sparkleOverlays.put(target, overlay);

        // Emit small bursts while active (hover)
        Timer emitter = new Timer(140, e -> overlay.emitBurst(5 + (int) (Math.random() * 6))); // 5..10
        emitter.start();
        sparkleEmitTimers.put(target, emitter);

        // kick start with an initial burst
        overlay.emitBurst(8);
    }

    public void stopSparkle(JComponent target) {
        Timer em = sparkleEmitTimers.remove(target);
        if (em != null) em.stop();

        SparkleOverlay ov = sparkleOverlays.remove(target);
        if (ov != null) ov.stopAndRemove();
    }

    public void sparkleFor(JComponent target, int durationMs) {
        sparkle(target, true); // start

        Timer stopTimer = new Timer(durationMs, e -> {
            sparkle(target, false); // stop
            ((Timer) e.getSource()).stop();
        });
        stopTimer.setRepeats(false);
        stopTimer.start();
    }


    //helpers
    public void stopAllAnimations() {
        active.values().forEach(Timer::stop);
    }

    public boolean isRunning() {
        return !active.isEmpty();
    }


    ////////////////////////////// sparkle overlay is a subclass used for extra layering of aninmation
    private static class SparkleOverlay extends JComponent {
        private static class Particles {
            float x, y;
            float vx, vy;
            float a;
            float life;
            int size;
            Color color;

            float startY;
            float maxTravel;
        }


        private final java.util.List<Particles> particles = new java.util.ArrayList<>();
        private final JComponent target;
        private final JRootPane root;
        private final JLayeredPane layeredPane;
        private final Timer twinkleTimer;
        private int twinkleFramesLeft = 0;


        private final Timer tick;
        private long lastTs = System.currentTimeMillis();

        SparkleOverlay(JComponent target, JRootPane root) {
            this.target = target;
            this.root = root;
            this.layeredPane = root.getLayeredPane();
            setOpaque(false);

            twinkleTimer = new Timer(700, e -> { // checks ~every 0.7s
                if (!target.isShowing()) return;

                // ~18% chance each check => roughly "every few seconds"
                if (Math.random() < 0.18) {
                    twinkleFramesLeft = 2; // 1–2 frames of extra glint
                }
            });
            twinkleTimer.start();


            // place overlay over the whole root so we don't fight layout
            setBounds(0, 0, root.getWidth(), root.getHeight());
            layeredPane.add(this, JLayeredPane.POPUP_LAYER);
            layeredPane.repaint();

            tick = new Timer(16, e -> onTick()); // ~60fps
            tick.start();
        }

        void emitBurst(int count) {
            setBounds(0, 0, root.getWidth(), root.getHeight());

            Point screenPos, rootPos;
            try {
                screenPos = target.getLocationOnScreen();
                rootPos = root.getLocationOnScreen();
            } catch (IllegalComponentStateException ex) {
                return;
            }

            int tx = screenPos.x - rootPos.x;
            int ty = screenPos.y - rootPos.y;

            int w = Math.max(1, target.getWidth());
            int h = Math.max(1, target.getHeight());

            for (int i = 0; i < count; i++) {
                Particles p = new Particles();

                p.x = tx + (float) (Math.random() * w);
                p.y = ty + (float) (Math.random() * (h * 0.6));

                p.vx = (float) ((Math.random() - 0.5) * 0.4);
                p.vy = (float) (-(0.6 + Math.random() * 0.6));

                p.startY = p.y;
                p.maxTravel = 50f;

                p.a = 1f;
                p.life = 450 + (float) (Math.random() * 350);

                p.size = 2 + (int) (Math.random() * 6);

                Color[] pal = { Color.WHITE, new Color(255, 240, 120), new Color(239, 73, 73) };
                p.color = pal[(int) (Math.random() * pal.length)];

                particles.add(p);
            }

            repaint();
        }


        void stopAndRemove() {
            tick.stop();
            twinkleTimer.stop();
            layeredPane.remove(this);
            layeredPane.repaint();
        }


        private void onTick() {
            long now = System.currentTimeMillis();
            float dt = (now - lastTs);
            lastTs = now;

            if (!target.isShowing() || root.getWidth() <= 0 || root.getHeight() <= 0) {
                stopAndRemove();
                return;
            }

            if (twinkleFramesLeft > 0) {
                emitCornerGlint();   // adds a few pixels instantly
                twinkleFramesLeft--;
            }

            // fade + move
            for (int i = particles.size() - 1; i >= 0; i--) {
                Particles particles = this.particles.get(i);
                particles.life -= dt;

                particles.x += particles.vx * dt;
                particles.y += particles.vy * dt;

                // fade out non-linearly for nicer look
                float t = Math.max(0f, particles.life) / 800f;
                particles.a = Math.min(1f, t * t);

                if (particles.life <= 0f) this.particles.remove(i);
            }

            // if nothing left, just keep ticking (emitter may add more)
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (particles.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g.create();
            // crisp pixels > antialias
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            for (Particles particles : this.particles) {
                if (particles.a <= 0f) continue;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, particles.a));

                int x = Math.round(particles.x);
                int y = Math.round(particles.y);

                g2.fillRect(x, y, particles.size, particles.size);

                // occasional second pixel for extra retro twinkle
                if (particles.size >= 3 && Math.random() < 0.15) {
                    g2.fillRect(x + particles.size + 1, y, 2, 2);
                }

                g2.setColor(particles.color);

            }

            g2.dispose();
        }

        private void emitCornerGlint() {
            Point screenPos, rootPos;
            try {
                screenPos = target.getLocationOnScreen();
                rootPos = root.getLocationOnScreen();
            } catch (IllegalComponentStateException ex) {
                return;
            }

            int tx = screenPos.x - rootPos.x;
            int ty = screenPos.y - rootPos.y;

            int w = Math.max(1, target.getWidth());
            int h = Math.max(1, target.getHeight());

            // pick a corner (0..3)
            int corner = (int)(Math.random() * 4);

            float cx, cy;
            int pad = 6;
            switch (corner) {
                case 0 -> { cx = tx + pad;       cy = ty + pad; }       // top-left
                case 1 -> { cx = tx + w - pad;   cy = ty + pad; }       // top-right
                case 2 -> { cx = tx + pad;       cy = ty + h - pad; }   // bottom-left
                default -> { cx = tx + w - pad;  cy = ty + h - pad; }   // bottom-right
            }

            // 3–5 “pixels” clustered, barely moving, fast fade
            int n = 3 + (int)(Math.random() * 3);

            Color[] pal = { Color.WHITE, new Color(255, 240, 140) };

            for (int i = 0; i < n; i++) {
                Particles p = new Particles();
                p.x = cx + (float)((Math.random() - 0.5) * 10);
                p.y = cy + (float)((Math.random() - 0.5) * 10);

                p.vx = 0f;
                p.vy = 0f;

                p.a = 1f;
                p.life = 80 + (float)(Math.random() * 80); // super short
                p.size = 2 + (int)(Math.random() * 2);

                p.color = pal[(int)(Math.random() * pal.length)];

                particles.add(p);
            }

            repaint();
        }



    }

    //this animation is shake specifically for the mine icon within the mine tile
    public void animateMineHit(TileView tile) {
        int frames = 10;
        int[] i = {0};

        Timer t = new Timer(15, e -> {
            i[0]++;
            int dx = (i[0] % 2 == 0) ? 2 : -2;
            tile.setIconOffsetX(dx);

            if (i[0] >= frames) {
                tile.setIconOffsetX(0);
                active.remove(tile);
                ((Timer) e.getSource()).stop();
            }
        });

        if (canAnimate(tile, t)) t.start();
    }
}

