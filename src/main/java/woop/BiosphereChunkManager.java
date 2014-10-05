package woop;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.IntCache;

public class BiosphereChunkManager extends WorldChunkManager
{
	private final World world;
	private final ModConfig config;
	
	private final long seed;
	private final Random rnd;

	private static long getNewSeed()
	{
		return (long)UUID.randomUUID().hashCode() | (((long)UUID.randomUUID().hashCode()) << 32);
	}
	
	public BiosphereChunkManager()
	{
		this(getNewSeed(), WoopMod.Biosphere);
	}

	public BiosphereChunkManager(World world)
	{
		this(world, world.getSeed(), WoopMod.Biosphere);
	}

	public BiosphereChunkManager(long worldSeed, WorldType worldType)
	{
		this(null, worldSeed, worldType);
	}
	
	public BiosphereChunkManager(World world, long worldSeed, WorldType worldType)
	{
		super(worldSeed, worldType);
		
		this.world = world;
		this.seed = worldSeed;
		this.rnd = new Random(this.seed);
		this.config = ModConfig.get(world);
	}

	/**
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	public boolean areBiomesViable(int i, int j, int k, List list)
	{
		return true;
	}

	/**
	 * Return an adjusted version of a given temperature based on the y height
	 */
	public float getTemperatureAtHeight(float f, int i)
	{
		return f;
	}

	public float getHumid(int i, int j)
	{
		float f = this.getBiomeGenAt(i, j).rainfall;
		return f <= 1.0F ? f : 1.0F;
	}

	/**
	 * Returns a list of rainfall values for the specified blocks. Args:
	 * listToReuse, x, z, width, length.
	 */
	public float[] getRainfall(float[] af, int i, int j, int k, int l)
	{
		IntCache.resetIntCache();

		if (af == null || af.length < k * l)
		{
			af = new float[k * l];
		}

		float f = this.getHumid(i, j);
		int i1 = 0;

		for (int j1 = 0; j1 < k; ++j1)
		{
			for (int k1 = 0; k1 < l; ++k1)
			{
				af[i1] = f;
				++i1;
			}
		}

		return af;
	}

	public float getTemp(int i, int j)
	{
		float f = this.getBiomeGenAt(i, j).temperature;
		return f <= 1.0F ? f : 1.0F;
	}

	/**
	 * Returns a list of temperatures to use for the specified blocks. Args:
	 * listToReuse, x, y, width, length
	 */
	public float[] getTemperatures(float[] af, int i, int j, int k, int l)
	{
		IntCache.resetIntCache();

		if (af == null || af.length < k * l)
		{
			af = new float[k * l];
		}

		float f = this.getTemp(i, j);
		int i1 = 0;

		for (int j1 = 0; j1 < k; ++j1)
		{
			for (int k1 = 0; k1 < l; ++k1)
			{
				af[i1] = f;
				++i1;
			}
		}

		return af;
	}

	/**
	 * Returns the BiomeGenBase related to the x, z position on the world.
	 */
	public BiomeGenBase getBiomeGenAt(int i, int j)
	{
		int k = i >> 4;
		int l = j >> 4;
		int i1 = (k - (int)Math.floor(Math.IEEEremainder((double)k, (double)config.getScaledGridSize())) << 4) + 8;
		int j1 = (l - (int)Math.floor(Math.IEEEremainder((double)l, (double)config.getScaledGridSize())) << 4) + 8;
		this.rnd.setSeed(this.seed);
		long l1 = this.rnd.nextLong() / 2L * 2L + 1L;
		long l2 = this.rnd.nextLong() / 2L * 2L + 1L;
		this.rnd.setSeed(((long)i1 * l1 + (long)j1 * l2) * 7215145L ^ this.seed);
		return ((BiomeEntry)WeightedRandom.getRandomItem(this.rnd, config.AllBiomes)).biome;
	}

	/**
	 * Returns an array of biomes for the location input.
	 */
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int i, int j, int k, int l)
	{
		return this.getBiomeGenAt(biomes, i, j, k, l, false);
	}

	/**
	 * Return a list of biomes for the specified blocks. Args: listToReuse, x,
	 * y, width, length, cacheFlag (if false, don't check biomeCache to avoid
	 * infinite loop in BiomeCacheBlock)
	 */
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int x, int z, int width, int length, boolean cacheFlag)
	{
		IntCache.resetIntCache();

		if (biomes == null || biomes.length < width * length)
		{
			biomes = new BiomeGenBase[width * length];
		}

		BiomeGenBase biomegenbase = this.getBiomeGenAt(x, z);
		int i1 = 0;

		for (int j1 = 0; j1 < width; ++j1)
		{
			for (int k1 = 0; k1 < length; ++k1)
			{
				biomes[i1++] = biomegenbase;
			}
		}

		return biomes;
	}

	/**
	 * Finds a valid position within a range, that is in one of the listed
	 * biomes. Searches {par1,par2} +-par3 blocks. Strongly favors positive y
	 * positions.
	 */
	public ChunkPosition findBiomePosition(int i, int j, int k, List list, Random random)
	{
		return new ChunkPosition(0, 64, 0);
	}

}
