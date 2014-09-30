package woop;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.storage.WorldInfo;

public class BiosphereWorldProvider extends WorldProviderSurface
{
	@Override
	public ChunkCoordinates getRandomizedSpawnPoint()
	{
		return super.getRandomizedSpawnPoint();
	}
	
	@Override
	public ChunkCoordinates getSpawnPoint()
    {
        return super.getSpawnPoint();
    }
	
	
//	private static final int maxY = 128;
//	private static final int minY = 0;
//	
//	
//	private boolean SpawnedOnTopOfDome(ChunkCoordinates coords)
//	{
//		if (coords == null) { return false; }
//
//		World world = super.worldObj;
//		if (world == null) { return false; }
//
//		int domeBlockCount = 0;
//
//		int topY = world.getTopSolidOrLiquidBlock(coords.posX, coords.posZ);
//		
//		return true;
//		
//		
////		
////		for (double yo = -10; yo <= 10; yo++)
////		{
////
////			int y = (int)Math.round(entity.posY + yo);
////
////			Block block = world.getBlock(x, y, z);
////
////			// System.out.println("SPAWN BLOCK [" + x + ", " + y + ", "
////			// + z
////			// + "](" + yo + "): " +
////			// WoopMod.GetNameOrIdForBlock(block));
////
////			if (block == BiosphereGen.DOME_TYPE || block == Blocks.end_stone || block == Blocks.netherrack)
////			{
////
////				domeBlockCount++;
////
////				if (domeBlockCount > 3)
////				{
////
////					// not dome world.
////					return false;
////				}
////			}
////			else if (block != Blocks.air)
////			{
////
////				if (block != Blocks.water && block != Blocks.flowing_water)
////				{
////
////					// not dome world.
////					return false;
////				}
////
////				// One of the blocks is water...
////				if (!BiosphereGen.WATERWORLD) { return false; }
////			}
////		}
////
////		return domeBlockCount >= 1; // spawned on top of a dome!!
//	}
//
//	private static boolean ValidSpawnLocation(Entity entity)
//	{
//
//		if (entity == null) { return true; }
//
//		World world = entity.worldObj;
//		if (world == null) { return true; }
//
//		int y = (int)Math.round(entity.posY);
//		int x = (int)Math.round(entity.posX);
//		int z = (int)Math.round(entity.posZ);
//
//		for (int i = 0; i < 3; i++)
//		{
//
//			Block block = world.getBlock(x, y + 1, z);
//
//			if (block != Blocks.air)
//			{
//				// trying to spawn in the middle of non-empty blocks
//				return false;
//			}
//		}
//
//		Block under = world.getBlock(x, y - 1, z);
//		return under != Blocks.air && under != BiosphereGen.DOME_TYPE;
//	}
//
//	public static boolean TryFixSpawnLocation(Entity entity)
//	{
//
//		if (entity == null) { return false; }
//
//		World world = entity.worldObj;
//		if (world == null) { return false; }
//
//		boolean printed = false;
//		boolean locationModified = false;
//
//		while (SpawnedOnTopOfDome(entity))
//		{
//
//			if (!printed)
//			{
//
//				printed = true;
//				// System.out.printf("Invalid Spawn Location, Fixing (%s).%n",
//				// GetName(entity));
//				// System.out.printf("    Org. Location: [%.2f, %.2f, %.2f]%n",
//				// entity.posX, entity.posY, entity.posZ);
//			}
//
//			entity.setPosition(
//				Math.round(entity.posX) - 0.5d,
//				Math.round(entity.posY) - 8.0d,
//				Math.round(entity.posZ) - 0.5d);
//
//			while (!ValidSpawnLocation(entity))
//			{
//
//				entity.setPosition(entity.posX, entity.posY - 1.0d, entity.posZ);
//			}
//
//			locationModified = true;
//		}
//
//		if (locationModified)
//		{
//			entity.setPosition(entity.posX, entity.posY + .5d, entity.posZ);
//
//			// System.out.printf("    Final Location: [%.2f, %.2f, %.2f]%n",
//			// entity.posX, entity.posY, entity.posZ);
//		}
//
//		return locationModified;
//	}

}