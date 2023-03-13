package stuff.world.blocks.logic;
import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.FrameBuffer;
import arc.struct.LongQueue;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.world.blocks.logic.LogicDisplay;

public class DenseLogicDisplay extends LogicDisplay{

    public DenseLogicDisplay(String name) {
        super(name);
        maxSides = 300;
        scaleFactor = 0.06f;
    }

    @Override
    public void setStats() {
        super.setStats();
    }

    static int unpackSign(int value){
        return (value & 0b10000000000000) * ((value & 0b01111111111111)) != 0 ? -1 : 1;
    }

    // These on top is what make this different, everything in below are the same with the LogicDisplayBuild class hhh.
    
    public class DenseLogicDisplayBuild extends Building{
        public FrameBuffer buffer;
        public float color = Color.whiteFloatBits;
        public float stroke = 1f;
        public LongQueue commands = new LongQueue(256);

        @Override
        public void draw() {
            super.draw();

            if(!Vars.renderer.drawDisplays) return;

            Draw.draw(Draw.z(), () -> {
                if(buffer == null){
                    buffer = new FrameBuffer(displaySize, displaySize);
                    buffer.begin(Pal.darkerMetal);
                    buffer.end();
                }
            });

            if(!commands.isEmpty()){
                Draw.draw(Draw.z(), () -> {
                    Tmp.m1.set(Draw.proj());
                    Draw.proj(0, 0, displaySize, displaySize);
                    buffer.begin();
                    Draw.color(color);
                    Lines.stroke(stroke);

                    while(!commands.isEmpty()){
                        long c = commands.removeFirst();
                        byte type = DisplayCmd.type(c);
                        int x = unpackSign(DisplayCmd.x(c)), y = unpackSign(DisplayCmd.y(c)),
                        p1 = unpackSign(DisplayCmd.p1(c)), p2 = unpackSign(DisplayCmd.p2(c)), p3 = unpackSign(DisplayCmd.p3(c)), p4 = unpackSign(DisplayCmd.p4(c));

                        switch(type){
                            case commandClear -> Core.graphics.clear(x / 255f, y / 255f, p1 / 255f, 1f);
                            case commandLine -> Lines.line(x, y, p1, p2);
                            case commandRect -> Fill.crect(x, y, p1, p2);
                            case commandLineRect -> Lines.rect(x, y, p1, p2);
                            case commandPoly -> Fill.poly(x, y, Math.min(p1, maxSides), p2, p3);
                            case commandLinePoly -> Lines.poly(x, y, Math.min(p1, maxSides), p2, p3);
                            case commandTriangle -> Fill.tri(x, y, p1, p2, p3, p4);
                            case commandColor -> Draw.color(this.color = Color.toFloatBits(x, y, p1, p2));
                            case commandStroke -> Lines.stroke(this.stroke = x);
                            case commandImage -> Draw.rect(Fonts.logicIcon(p1), x, y, p2, p2, p3);
                        }
                    }

                    buffer.end();
                    Draw.proj(Tmp.m1);
                    Draw.reset();
                });
            }

            Draw.blend(Blending.disabled);
            Draw.draw(Draw.z(), () -> {
                if(buffer != null){
                    Draw.rect(Draw.wrap(buffer.getTexture()), x, y, buffer.getWidth() * scaleFactor * Draw.scl, -buffer.getHeight() * scaleFactor * Draw.scl);
                }
            });
            Draw.blend();
        }

        @Override
        public void remove() {
            super.remove();
            if(buffer != null){
                buffer.dispose();
                buffer = null;
            }
        }
    }
}
