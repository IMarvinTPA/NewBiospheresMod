package woop;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.*;

@Mod(modid = WoopMod.MODID, version = WoopMod.VERSION)
public class WoopMod {
	public static final String MODID = "WoopMod";
	public static final String VERSION = "Ver. 0";

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		// some example code
		System.out.println("DIRT BLOCK >> " + Blocks.dirt.getUnlocalizedName());

		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.diamond_block), new ItemStack(Blocks.dirt));
	}
}