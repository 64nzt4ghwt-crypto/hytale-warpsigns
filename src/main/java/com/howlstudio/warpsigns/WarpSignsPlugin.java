package com.howlstudio.warpsigns;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
/** WarpSigns — Create labeled warp signs. Players interact with sign to trigger warp. Admin /warpsign create. */
public final class WarpSignsPlugin extends JavaPlugin {
    private WarpSignManager mgr;
    public WarpSignsPlugin(JavaPluginInit init){super(init);}
    @Override protected void setup(){
        System.out.println("[WarpSigns] Loading...");
        mgr=new WarpSignManager(getDataDirectory());
        CommandManager.get().register(mgr.getWarpSignCommand());
        CommandManager.get().register(mgr.getWarpSignListCommand());
        System.out.println("[WarpSigns] Ready. "+mgr.getCount()+" warp signs.");
    }
    @Override protected void shutdown(){if(mgr!=null)mgr.save();System.out.println("[WarpSigns] Stopped.");}
}
