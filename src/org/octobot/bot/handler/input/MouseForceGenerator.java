package org.octobot.bot.handler.input;

import org.octobot.script.Condition;
import org.octobot.script.methods.Mouse;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MouseTest
 *
 * @author Pat-ji
 */
public class MouseForceGenerator {
    private final Point target;
    private final Vector velocity;
    private final List<ForceModifier> forceModifiers;

    public boolean active, completed;

    public MouseForceGenerator() {
        target = new Point(-1, -1);
        velocity = new Vector();
        forceModifiers = new ArrayList<ForceModifier>();

        active = true;
    }

    public boolean hover(final Mouse mouse, final Model model) {
        if (!model.isOnGameScreen()) {
            active = false;
            return false;
        }

        if (target.x == -1 || target.y == -1 || !model.contains(target)) {
            final Point viewPortPoint = model.getRandomPoint();
            if (viewPortPoint != null) {
                if (!mouse.context().calculations.isOnScreen(viewPortPoint.x, viewPortPoint.y)) {
                    Time.sleep(Random.nextInt(25, 50));
                    return false;
                }

                target.setLocation(viewPortPoint);
            }
        } else if (!mouse.context().calculations.isOnScreen(target.x, target.y)) {
            target.setLocation(-1, -1);
            Time.sleep(Random.nextInt(100, 200));
            return false;
        }

        final Point currentPoint = mouse.getLocation();
        if (target.equals(currentPoint) || model.contains(currentPoint)) {
            active = false;
            completed = true;
            return true;
        }

        if (forceModifiers.isEmpty())
            setup(mouse);

        final double deltaTime = Random.nextDouble(7.0D, 10.0D) / 1000.0D;
        final Vector forceVector = new Vector();
        for (final ForceModifier modifier : forceModifiers) {
            final Vector vector = modifier.apply(deltaTime, target);
            if (vector != null)
                forceVector.add(vector);
        }

        if (Double.isNaN(forceVector.xUnits) || Double.isNaN(forceVector.yUnits)) {
            active = false;
            return false;
        }

        velocity.add(forceVector.multiply(deltaTime));
        final Vector deltaPosition = velocity.multiply(deltaTime);
        if (deltaPosition.xUnits != 0 && deltaPosition.yUnits != 0) {
            mouse.hop((int) currentPoint.getX() + (int) deltaPosition.xUnits, (int) currentPoint.getY() + (int) deltaPosition.yUnits);
            Time.sleep(mouse.getSpeed());
            return true;
        }

        return new Condition() {
            @Override
            public boolean validate() {
                return model.contains(mouse.getLocation());
            }
        }.validate();
    }

    private void setup(final Mouse mouse) {
        forceModifiers.add(new ForceModifier() {
            //Target tracking
            public Vector apply(final double delta, final Point direction) {
                final Point currentLocation = mouse.getLocation();
                final Vector targetVector = new Vector();
                targetVector.xUnits = direction.x - currentLocation.getX();
                targetVector.yUnits = direction.y - currentLocation.getY();
                if (targetVector.xUnits == 0 && targetVector.yUnits == 0) {
                    return null;
                }

                final double angle = targetVector.getAngle();
                final double acceleration = Random.nextInt(2500, 3000);
                final Vector force = new Vector();
                force.xUnits = Math.cos(angle) * acceleration;
                force.yUnits = Math.sin(angle) * acceleration;
                return force;
            }
        });

        forceModifiers.add(new ForceModifier() {
            //Friction
            public Vector apply(final double delta, final Point direction) {
                return velocity.multiply(-1);
            }
        });

        forceModifiers.add(new ForceModifier() {
            //Velocity killer on destination (prevent loop-back)
            public Vector apply(final double delta, final Point direction) {
                final Point currentLocation = mouse.getLocation();
                final Vector targetVector = new Vector();
                targetVector.xUnits = direction.x - currentLocation.getX();
                targetVector.yUnits = direction.y - currentLocation.getY();
                if (targetVector.xUnits > -3 && targetVector.xUnits < 3 &&
                        targetVector.yUnits > -3 && targetVector.yUnits < -3) {
                    velocity.xUnits = 0;
                    velocity.yUnits = 0;
                }

                return null;
            }
        });

        forceModifiers.add(new ForceModifier() {
            //Target noise
            public Vector apply(final double delta, final Point direction) {
                final Point currentLocation = mouse.getLocation();
                final Vector targetVector = new Vector();
                targetVector.xUnits = direction.x - currentLocation.getX();
                targetVector.yUnits = direction.y - currentLocation.getY();
                final double targetMagnitude = targetVector.getMagnitude();
                if (targetMagnitude > Random.nextInt(0, 10)) {
                    final double angle = Random.nextDouble(-Math.PI, Math.PI);
                    final Vector force = new Vector();
                    final int acceleration = Random.nextInt(2000, 4000);
                    force.xUnits = Math.cos(angle) * acceleration;
                    force.yUnits = Math.sin(angle) * acceleration;
                    return force;
                }

                return null;
            }
        });

        forceModifiers.add(new ForceModifier() {
            //Pass near-target fix (high-velocity curve)
            public Vector apply(final double delta, final Point direction) {
                final Point currentLocation = mouse.getLocation();
                final Vector targetVector = new Vector();
                targetVector.xUnits = direction.x - currentLocation.getX();
                targetVector.yUnits = direction.y - currentLocation.getY();

                final double targetMagnitude = targetVector.getMagnitude();
                if (targetMagnitude < Random.nextInt(120, 200)) {
                    final double targetLength = Math.pow(targetMagnitude, 2);
                    if (targetLength == 0) return null;

                    final double computedLength = Math.sqrt(Math.pow(velocity.getMagnitude(), 2) / targetLength);
                    final Vector adjustedToTarget = targetVector.multiply(computedLength);

                    Vector force = new Vector();
                    force.xUnits = (adjustedToTarget.xUnits - velocity.xUnits) / (delta);
                    force.yUnits = (adjustedToTarget.yUnits - velocity.yUnits) / (delta);

                    final double adjustmentFactor = 8D / targetMagnitude;
                    if (adjustmentFactor < 1D)
                        force = force.multiply(adjustmentFactor);

                    if (targetMagnitude < 10D)
                        force = force.multiply(0.5D);

                    return force;
                }

                return null;
            }
        });
    }

}
