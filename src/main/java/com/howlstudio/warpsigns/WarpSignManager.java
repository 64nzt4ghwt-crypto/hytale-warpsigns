package com.howlstudio.warpsigns;
import com.hypixel.hytale.component.Ref; import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.nio.file.*; import java.util.*;
public class WarpSignManager {
    private final Path dataDir;
    // label → destination warp name
    private final Map<String,String> signs=new LinkedHashMap<>();
    public WarpSignManager(Path d){this.dataDir=d;try{Files.createDirectories(d);}catch(Exception e){}load();}
    public int getCount(){return signs.size();}
    public void save(){try{StringBuilder sb=new StringBuilder();for(var e:signs.entrySet())sb.append(e.getKey()+"|"+e.getValue()+"\n");Files.writeString(dataDir.resolve("signs.txt"),sb.toString());}catch(Exception e){}}
    private void load(){try{Path f=dataDir.resolve("signs.txt");if(!Files.exists(f))return;for(String l:Files.readAllLines(f)){String[]p=l.split("\\|",2);if(p.length==2)signs.put(p[0],p[1]);}}catch(Exception e){}}
    public AbstractPlayerCommand getWarpSignCommand(){
        return new AbstractPlayerCommand("warpsign","[Admin] Manage warp signs. /warpsign create <label> <warp> | del <label> | trigger <label>"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                String[]args=ctx.getInputString().trim().split("\\s+",3);String sub=args.length>0?args[0]:"list";
                switch(sub.toLowerCase()){
                    case"create"->{if(args.length<3){playerRef.sendMessage(Message.raw("Usage: /warpsign create <label> <warp>"));break;}signs.put(args[1].toLowerCase(),args[2].toLowerCase());save();playerRef.sendMessage(Message.raw("[WarpSigns] Created sign §6"+args[1]+"§r → warp §e"+args[2]));}
                    case"del"->{if(args.length<2)break;String removed=signs.remove(args[1].toLowerCase());save();playerRef.sendMessage(removed!=null?Message.raw("[WarpSigns] Removed sign: "+args[1]):Message.raw("[WarpSigns] Not found: "+args[1]));}
                    case"trigger"->{if(args.length<2)break;String dest=signs.get(args[1].toLowerCase());if(dest==null){playerRef.sendMessage(Message.raw("[WarpSigns] Sign not found: "+args[1]));break;}playerRef.sendMessage(Message.raw("[WarpSigns] §aTeleporting to §6"+dest+"§r via sign §e"+args[1]+"§r..."));}
                    default->playerRef.sendMessage(Message.raw("Usage: /warpsign create|del|trigger|list"));
                }
            }
        };
    }
    public AbstractPlayerCommand getWarpSignListCommand(){
        return new AbstractPlayerCommand("warpsigns","List all warp signs. /warpsigns"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                if(signs.isEmpty()){playerRef.sendMessage(Message.raw("[WarpSigns] No warp signs defined."));return;}
                playerRef.sendMessage(Message.raw("=== Warp Signs ("+signs.size()+") ==="));
                for(var e:signs.entrySet())playerRef.sendMessage(Message.raw("  §6["+e.getKey()+"]§r → §e"+e.getValue()));
            }
        };
    }
}
