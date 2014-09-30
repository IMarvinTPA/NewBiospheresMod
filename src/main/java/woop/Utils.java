package woop;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class Utils
{
	public static Block ParseBlock(String blockNameOrId)
	{
		try
		{
			int id = Integer.parseInt(blockNameOrId);
			return Block.getBlockById(id);
		}
		catch (NumberFormatException ignore)
		{
			return Block.getBlockFromName(blockNameOrId);
		}
	}

	public static String GetNameOrIdForBlock(Block block)
	{
		if (block == null) { return "air"; }

		String ret = null;

		try
		{
			ret = Block.blockRegistry.getNameForObject(block);
		}
		catch (Exception ignore)
		{ /* do nothing */
		}

		if (ret == null || ret.length() < 1)
		{
			ret = Integer.toString(Block.getIdFromBlock(block));
		}

		return ret;
	}

	public static String GetName(Object obj)
	{
		if (obj == null) { return "(null)"; }

		if (obj instanceof Block) { return GetNameOrIdForBlock((Block)obj); }

		String name = obj.getClass().getSimpleName();
		if (name == null || name.length() < 1)
		{
			name = obj.getClass().getName();
		}

		return name;
	}

	public static boolean IsPlayer(Entity e)
	{
		if (e == null) { return false; }

		if (e instanceof EntityPlayer) { return true; }

		if (e instanceof EntityLivingBase)
		{
			Class c = e.getClass();

			while (c != null)
			{
				try
				{
					java.lang.reflect.Method m = c.getDeclaredMethod("isPlayer");
					m.setAccessible(true);

					boolean result = ((Boolean)m.invoke(e)).booleanValue();
					return result;
				}
				catch (Throwable ignore)
				{
					c = c.getSuperclass();
				}
			}
		}

		return false;
	}

	public static ChunkCoordinates GetCoords(Entity e)
	{
		if (e == null) { return GetCoords(0,0,0); }
		return GetCoords(e.posX, e.posY, e.posZ);
	}
	
	public static ChunkCoordinates GetCoords(double x, double y, double z)
	{
		return GetCoords((int)Math.round(x), (int)Math.round(y), (int)Math.round(z));
	}

	public static ChunkCoordinates GetCoords(int x, int y, int z)
	{
		ChunkCoordinates coords = new ChunkCoordinates();
		coords.posX = x;
		coords.posY = y;
		coords.posZ = z;

		return coords;
	}
}