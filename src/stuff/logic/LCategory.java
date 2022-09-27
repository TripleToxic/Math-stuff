package stuff.logic;

import arc.*;
import arc.graphics.*;
import arc.scene.style.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class LCategory implements Comparable<LCategory>{
    public static final Seq<LCategory> all = new Seq<>();

    public static final LCategory

    operation = new LCategory("operation", Pal.logicOperations, Icon.settingsSmall);

    public final String name;
    public final int id;
    public final Color color;

    @Nullable
    public final Drawable icon;

    public LCategory(String name, Color color){
        this(name, color,null);
    }

    public LCategory(String name, Color color, Drawable icon){
        this.icon = icon;
        this.color = color;
        this.name = name;
        id = all.size;
        all.add(this);
    }

    public String localized(){
        return Core.bundle.get("lcategory." + name);
    }

    public String description(){
        return Core.bundle.get("lcategory." + name + ".description");
    }

    @Override
    public int compareTo(LCategory o){
        return id - o.id;
    }
}
