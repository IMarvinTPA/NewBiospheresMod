package woop;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import akka.japi.Creator;
import akka.japi.Predicate;

public class BiosphereChunkProvider implements IChunkProvider
{
	public final World world;
	public final ModConfig config;

	/**
	 * Get whether the map features (e.g. strongholds) generation is enabled or disabled.
	 */
	public boolean getMapFeaturesEnabled()
	{
		return world.getWorldInfo().isMapFeaturesEnabled();
	}

	private MapGenBase caveGen = new BiosphereMapGen();
	//
	private NoiseGeneratorOctaves noiseGen;
	// public double noiseMin = Double.MAX_VALUE;
	// public double noiseMax = Double.MIN_VALUE;
	// public double[] noise = new double[256];

	public static final int zShift = 7;
	public static final int xShift = 11;
	public final long worldSeed;

	private final LruCacheList<SphereChunk> chunks = new LruCacheList<SphereChunk>(10);

	public synchronized SphereChunk GetSphereChunk(final int chunkX, final int chunkZ)
	{
		final BiosphereChunkProvider _this = this;
		
		return chunks.FindOrAdd(new Predicate<SphereChunk>()
		{
			public boolean test(SphereChunk chunk)
			{
				return chunk.chunkX == chunkX && chunk.chunkZ == chunkZ;
			}
		}, new Creator<SphereChunk>()
		{
			public SphereChunk create()
			{
				return new SphereChunk(_this, chunkX, chunkZ);
			}
		});
	}

	public BiosphereChunkProvider(World world)
	{
		this.world = world;
		this.worldSeed = world.getSeed();
		this.config = ModConfig.get(world);

		// if (NOISE)
		// {
		// this.rndNoise = new Random(seed);
		// this.noiseGen = new NoiseGeneratorOctaves(this.rndNoise, 4);
		// }
		// else
		// {
		// this.rndNoise = null;
		// }
	}

	// public void setRand(int chunkX, int chunkZ)
	// {
	// ChunkCoordinates cc = GetSphereCenter(chunkX, chunkZ);
	//
	// this.midX = cc.posX;
	// this.midZ = cc.posZ;
	//
	// this.oreMidX = this.midX + this.scaledGrid / 2 * 16 - this.scaledSpecial;
	// this.oreMidZ = this.midZ + this.scaledGrid / 2 * 16 - this.scaledSpecial;
	//
	// this.rndSphere.setSeed(this.world.getSeed());
	// long l = this.rndSphere.nextLong() / 2L * 2L + 1L;
	// long l1 = this.rndSphere.nextLong() / 2L * 2L + 1L;
	// long l2 = ((long)this.midX * l + (long)this.midZ * l1) * 2512576L ^ this.world.getSeed();
	// this.rndSphere.setSeed(l2);
	//
	// this.sphereRadius = (double)((float)Math.round(16.0D + this.rndSphere.nextDouble() * 32.0D
	// + this.rndSphere.nextDouble() * 16.0D) * scale);
	// this.lakeRadius = (double)Math.round(this.sphereRadius / 4.0D);
	// this.lakeEdgeRadius = this.lakeRadius + 2.0D;
	// this.biome = this.world.getWorldChunkManager().getBiomeGenAt(chunkX << 4, chunkZ << 4);
	// this.lavaLake = this.biome == BiomeGenBase.hell || this.biome != BiomeGenBase.swampland
	// && this.biome != BiomeGenBase.taiga && this.biome != BiomeGenBase.icePlains
	// && this.biome != BiomeGenBase.sky && this.rndSphere.nextInt(10) == 0;
	// this.hasLake = this.biome == BiomeGenBase.swampland || this.biome != BiomeGenBase.sky
	// && this.rndSphere.nextInt(2) == 0;
	// this.oreMidY = this.scaledSpecial + 1 + this.rndSphere.nextInt(worldMaxY - (this.scaledSpecial + 1));
	//
	// if (NOISE)
	// {
	// this.setNoise(this.midX >> 4, this.midZ >> 4);
	// this.noiseMin = Double.MAX_VALUE;
	//
	// for (int k = 0; k < this.noise.length; ++k)
	// {
	// if (this.noise[k] < this.noiseMin)
	// {
	// this.noiseMin = this.noise[k];
	// }
	// }
	//
	// this.lakeMidY = (int)Math.round(seaLevel + this.noiseMin * 8.0D * 1.0D);
	// this.setNoise(chunkX, chunkZ);
	// }
	// else
	// {
	// this.lakeMidY = this.midY;
	// }
	// }
	//

	public void setNoise(int x, int z)
	{
		// if (NOISE)
		// {
		// double d = 0.0078125D;
		// this.noise = this.noiseGen.generateNoiseOctaves(this.noise, x * 16, worldHeight, z * 16, 16, 1, 16, d, 1.0D,
		// d);
		// }
	}

	public void preGenerateChunk(int chunkX, int chunkZ, Block[] blocks)
	{
		SphereChunk chunk = GetSphereChunk(chunkX, chunkZ);
		Random rnd = chunk.GetPhaseRandom("preGenerateChunk");

		int rawX = chunkX << 4;
		int rawZ = chunkZ << 4;

		for (int zo = 0; zo < 16; ++zo)
		{
			for (int xo = 0; xo < 16; ++xo)
			{
				int midY = chunk.getSurfaceLevel(xo, zo);

				for (int rawY = ModConsts.WORLD_MAX_Y; rawY >= ModConsts.WORLD_MIN_Y; rawY--)
				{
					int idx = (xo << xShift) | (zo << zShift) | rawY;
					// Block block = (rawY <= (SEA_LEVEL - 10)) ? Blocks.water : Blocks.air;
					Block block = Blocks.air;

					int sphereDistance = chunk.getMainDistance(rawX + xo, rawY, rawZ + zo);
					int oreDistance = chunk.getOrbDistance(rawX + xo, rawY, rawZ + zo);

					if (rawY > midY)
					{
						if (sphereDistance == chunk.radius)
						{
							if (rawY >= midY + 4 || Math.abs(rawX + xo - chunk.sphereLocation.posX) > config.getBridgeWidth()
									&& Math.abs(rawZ + zo - chunk.sphereLocation.posZ) > config.getBridgeWidth())
							{
								block = config.getDomeBlock();
							}
						}
						else if (chunk.hasLake && config.getNoiseEnabled() && chunk.biome != BiomeGenBase.desert
								&& (sphereDistance > chunk.lakeRadius && sphereDistance <= chunk.lakeEdgeRadius))
						{
							if (rawY == chunk.lakeLocation.posY)
							{
								block = chunk.biome.topBlock;
							}
							else if (rawY < chunk.lakeLocation.posY)
							{
								block = chunk.biome.fillerBlock;
							}
						}
						else if (chunk.hasLake && config.getNoiseEnabled() && chunk.biome != BiomeGenBase.desert
								&& sphereDistance <= chunk.lakeRadius)
						{
							if (rawY == chunk.lakeLocation.posY && chunk.biome == BiomeGenBase.icePlains)
							{
								block = Blocks.ice;
							}
							else if (rawY <= chunk.lakeLocation.posY)
							{
								block = (chunk.lavaLake ? Blocks.flowing_lava : Blocks.flowing_water);
							}
						}
						else if (config.doesNeedProtectionGlass()
								&& rawY <= midY + 4
								&& sphereDistance > chunk.radius
								&& (Math.abs(rawX + xo - chunk.sphereLocation.posX) == config.getBridgeWidth() || Math.abs(rawZ
										+ zo - chunk.sphereLocation.posZ) == config.getBridgeWidth()))
						{
							block = config.getDomeBlock();
						}
						else if (config.doesNeedProtectionGlass()
								&& rawY == midY + 4
								&& sphereDistance > chunk.radius
								&& (Math.abs(rawX + xo - chunk.sphereLocation.posX) < config.getBridgeWidth() || Math.abs(rawZ
										+ zo - chunk.sphereLocation.posZ) < config.getBridgeWidth()))
						{
							block = config.getDomeBlock();
						}
						else if (config.doesNeedProtectionGlass()
								&& rawY < midY + 4
								&& sphereDistance > chunk.radius
								&& (Math.abs(rawX + xo - chunk.sphereLocation.posX) < config.getBridgeWidth() || Math.abs(rawZ
										+ zo - chunk.sphereLocation.posZ) < config.getBridgeWidth()))
						{
							block = Blocks.air;
						}
						else if (config.doesNeedProtectionGlass() && sphereDistance > chunk.radius)
						{
							block = config.getOutsideFillerBlock();
						}
						else if (rawY == midY + 1
								&& sphereDistance > chunk.radius
								&& (Math.abs(rawX + xo - chunk.sphereLocation.posX) == config.getBridgeWidth() || Math.abs(rawZ
										+ zo - chunk.sphereLocation.posZ) == config.getBridgeWidth()))
						{
							block = config.getBridgeRailBlock();
						}
					}
					else if (sphereDistance == chunk.radius)
					{
						block = Blocks.stone;
					}
					else if (chunk.hasLake && chunk.biome != BiomeGenBase.desert && sphereDistance <= chunk.lakeRadius)
					{
						if (rawY == chunk.lakeLocation.posY && chunk.biome == BiomeGenBase.icePlains)
						{
							block = Blocks.ice;
						}
						else if (rawY <= chunk.lakeLocation.posY)
						{
							block = (chunk.lavaLake ? Blocks.flowing_lava : Blocks.flowing_water);
						}
					}
					else if (chunk.hasLake && rawY < chunk.lakeLocation.posY - 1 && chunk.biome != BiomeGenBase.desert
							&& sphereDistance <= chunk.lakeEdgeRadius)
					{
						block = (chunk.lavaLake ? Blocks.gravel : Blocks.sand);
					}
					else if (sphereDistance < chunk.radius)
					{
						if (rawY == midY)
						{
							block = chunk.biome.topBlock;
						}
						else if (rawY == midY - 1)
						{
							block = chunk.biome.fillerBlock;
						}
						else
						{
							block = Blocks.stone;
						}
					}
					else if (rawY == midY
							&& sphereDistance > chunk.radius
							&& (Math.abs(rawX + xo - chunk.sphereLocation.posX) < config.getBridgeWidth() + 1 || Math.abs(rawZ + zo
									- chunk.sphereLocation.posZ) < config.getBridgeWidth() + 1))
					{
						block = config.getBridgeSupportBlock();
					}
					else if (config.doesNeedProtectionGlass() && sphereDistance > chunk.radius)
					{
						block = config.getOutsideFillerBlock();
					}

					if (oreDistance == config.getScaledOrbRadius())
					{
						block = Blocks.glass;
					}
					else if (oreDistance < config.getScaledOrbRadius())
					{
						int oreChance = rnd.nextInt(500);

						if (oreChance < 5) // 1%
						{
							block = Blocks.lapis_ore;
						}
						else if (oreChance < 10) // 1%
						{
							block = Blocks.emerald_ore;
						}
						else if (oreChance < 15) // 1%
						{
							block = Blocks.diamond_ore;
						}
						else if (oreChance < 25) // 2%
						{
							block = Blocks.iron_ore;
						}
						else if (oreChance < 35) // 2%
						{
							block = Blocks.gold_ore;
						}
						else if (oreChance < 50) // 3%
						{
							block = Blocks.coal_ore;
						}
						else if (oreChance < 65) // 3%
						{
							block = Blocks.redstone_ore;
						}
						else if (oreChance < 75) // 2%
						{
							block = Blocks.quartz_ore;
						}
						else if (oreChance < 175) // 20%
						{
							block = Blocks.gravel;
						}
						else if (oreChance < 190) // 3%
						{
							block = Blocks.lava;
						}
						else
						// 62%
						{
							block = Blocks.stone;
						}
					}

					blocks[idx] = block;
				}
			}
		}
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	public Chunk loadChunk(int x, int z)
	{
		return this.provideChunk(x, z);
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
	 * specified chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(int x, int z)
	{
		// this.setRand(x, z);
		Block[] blocks = new Block[16 * 16 * ModConsts.WORLD_HEIGHT];

		this.preGenerateChunk(x, z, blocks);
		this.caveGen.func_151539_a(this, this.world, x, z, blocks); // func_151539_a == generate

		Chunk chunk = new Chunk(this.world, blocks, x, z);
		chunk.generateSkylightMap();

		return chunk;
	}

	/**
	 * Checks to see if a chunk exists at x, z
	 */
	public boolean chunkExists(int x, int z)
	{
		return true;
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ)
	{
		SphereChunk chunk = GetSphereChunk(chunkX, chunkZ);
		Random rnd = chunk.GetPhaseRandom("populate");

		BlockSand.fallInstantly = true;
		int absX = chunkX << 4;
		int absZ = chunkZ << 4;

		for (int i = 0; i < 10; i++)
		{
			int x = absX + rnd.nextInt(16);
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16);
			(new WorldGenClay(4)).generate(this.world, rnd, x, y, z);
		}

		for (int i = 0; i < 20; i++)
		{
			int x = absX + rnd.nextInt(16);
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16);
			(new WorldGenMinable(Blocks.coal_ore, 16)).generate(this.world, rnd, x, y, z);
		}

		for (int i = 0; i < 20; i++)
		{
			int x = absX + rnd.nextInt(16);
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16);
			(new WorldGenMinable(Blocks.iron_ore, 8)).generate(this.world, rnd, x, y, z);
		}

		for (int i = 0; i < 2; i++)
		{
			int x = absX + rnd.nextInt(16);
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16);
			(new WorldGenMinable(Blocks.gold_ore, 8)).generate(this.world, rnd, x, y, z);
		}

		for (int i = 0; i < 8; i++)
		{
			int x = absX + rnd.nextInt(16);
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16);
			(new WorldGenMinable(Blocks.redstone_ore, 7)).generate(this.world, rnd, x, y, z);
		}

		int treesPerChunk = chunk.biome.theBiomeDecorator.treesPerChunk;

		if (rnd.nextInt(10) == 0)
		{
			treesPerChunk++;
		}

		for (int i = 0; i < treesPerChunk; i++)
		{
			int x = absX + rnd.nextInt(16) + 8;
			int z = absZ + rnd.nextInt(16) + 8;
			int y = this.world.getHeightValue(x, z);

			// func_150567_a == getRandomWorldGenForTrees
			WorldGenerator gen = chunk.biome.func_150567_a(rnd);

			gen.setScale(config.getScale(), config.getScale(), config.getScale());
			gen.generate(this.world, rnd, x, y, z);
		}

		for (int i = 0; i < 2; i++)
		{
			int x = absX + rnd.nextInt(16) + 8;
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16) + 8;

			(new WorldGenFlowers(Blocks.yellow_flower)).generate(this.world, rnd, x, y, z);
		}

		if (rnd.nextInt(2) == 0)
		{
			int x = absX + rnd.nextInt(16) + 8;
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16) + 8;
			(new WorldGenFlowers(Blocks.red_flower)).generate(this.world, rnd, x, y, z);
		}

		if (rnd.nextInt(4) == 0)
		{
			int x = absX + rnd.nextInt(16) + 8;
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16) + 8;
			(new WorldGenFlowers(Blocks.brown_mushroom)).generate(this.world, rnd, x, y, z);
		}

		if (rnd.nextInt(8) == 0)
		{
			int x = absX + rnd.nextInt(16) + 8;
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16) + 8;
			(new WorldGenFlowers(Blocks.red_mushroom)).generate(this.world, rnd, x, y, z);
		}

		int l13;

		if (config.isTallGrassEnabled())
		{
			int grassPerChunk = chunk.biome.theBiomeDecorator.grassPerChunk;

			for (int i = 0; i < grassPerChunk; i++)
			{
				byte metadata = 1; // grass height maybe?

				if (chunk.biome == BiomeGenBase.desert && rnd.nextInt(3) != 0)
				{
					metadata = 2;
				}

				int x = absX + rnd.nextInt(16) + 8;
				int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
				int z = absZ + rnd.nextInt(16) + 8;

				(new WorldGenTallGrass(Blocks.tallgrass, metadata)).generate(this.world, rnd, x, y, z);
			}
		}

		for (int i = 0; i < 20; i++)
		{
			int x = absX + rnd.nextInt(16) + 8;
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16) + 8;
			(new WorldGenReed()).generate(this.world, rnd, x, y, z);
		}

		if (rnd.nextInt(32) == 0)
		{
			int x = absX + rnd.nextInt(16) + 8;
			int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);
			int z = absZ + rnd.nextInt(16) + 8;
			(new WorldGenPumpkin()).generate(this.world, rnd, x, y, z);
		}

		if (chunk.biome == BiomeGenBase.desert)
		{
			int count = rnd.nextInt(5);

			for (int i = 0; i < count; i++)
			{
				int x = absX + rnd.nextInt(16) + 8;
				int z = absZ + rnd.nextInt(16) + 8;
				int y = this.world.getHeightValue(x, z);

				(new WorldGenCactus()).generate(this.world, rnd, x, y, z);
			}
		}
		else if (chunk.biome == BiomeGenBase.hell)
		{
			if (rnd.nextBoolean())
			{
				int x = absX + rnd.nextInt(16) + 8;
				int z = absZ + rnd.nextInt(16) + 8;
				int y = this.world.getHeightValue(x, z);

				(new WorldGenFire()).generate(this.world, rnd, x, y, z);
			}
		}
		else if (chunk.biome == BiomeGenBase.mushroomIsland)
		{
			for (int i = 0; i < 2; i++)
			{
				int x = absX + rnd.nextInt(16) + 8;
				int z = absZ + rnd.nextInt(16) + 8;
				int y = this.world.getHeightValue(x, z);

				(new WorldGenBigMushroom()).generate(this.world, rnd, x, y, z);
			}

			for (int i = 0; i < 1; i++)
			{
				if (rnd.nextInt(4) == 0)
				{
					int x = absX + rnd.nextInt(16) + 8;
					int z = absZ + rnd.nextInt(16) + 8;
					int y = this.world.getHeightValue(x, z);

					(new WorldGenFlowers(Blocks.yellow_flower)).generate(this.world, rnd, x, y, z);
				}

				if (rnd.nextInt(8) == 0)
				{
					int x = absX + rnd.nextInt(16) + 8;
					int z = absZ + rnd.nextInt(16) + 8;
					int y = rnd.nextInt(ModConsts.WORLD_HEIGHT);

					(new WorldGenFlowers(Blocks.red_flower)).generate(this.world, rnd, x, y, z);
				}
			}
		}
		else if (chunk.biome == BiomeGenBase.taiga || chunk.biome == BiomeGenBase.icePlains)
		{
			// this.setNoise(chunkX, chunkZ);

			for (int zo = 0; zo < 16; zo++)
			{
				for (int xo = 0; xo < 16; xo++)
				{
					int midY = chunk.getSurfaceLevel(xo, zo);

					int x = xo + absX;
					int z = zo + absZ;
					int y = midY + 1;

					int distance = chunk.getMainDistance(x, midY, z);

					if (distance <= chunk.radius && this.world.isBlockFreezable(x, y, z))
					{
						this.world.setBlock(x, y, z, Blocks.snow);
					}
				}
			}
		}

		SpawnerAnimals.performWorldGenSpawning(this.world, chunk.biome, absX + 8, absZ + 8, 16, 16, rnd);
		BlockSand.fallInstantly = false;
	}

	/**
	 * Two modes of operation: if passed true, save all Chunks in one go. If passed false, save up to two chunks. Return
	 * true if all chunks have been saved.
	 */
	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
	{
		return true;
	}

	/**
	 * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
	 */
	public boolean unloadQueuedChunks()
	{
		return false;
	}

	/**
	 * Returns if the IChunkProvider supports saving.
	 */
	public boolean canSave()
	{
		return true;
	}

	/**
	 * Converts the instance data to a readable string.
	 */
	public String makeString()
	{
		return "RandomLevelSource";
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the given location.
	 */
	public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k)
	{
		BiomeGenBase biomegenbase = this.world.getBiomeGenForCoords(i, k);
		return biomegenbase == null ? null : biomegenbase.getSpawnableList(enumcreaturetype);
	}

	/**
	 * Returns the location of the closest structure of the specified type. If not found returns null.
	 */
	public ChunkPosition findClosestStructure(World world1, String s, int i, int j, int k)
	{
		return null;
	}

	public int getLoadedChunkCount()
	{
		return 0;
	}

	public void recreateStructures(int var1, int var2)
	{}

	public void func_104112_b()
	{}

	public void saveExtraData()
	{
		/* do nothing */
	}

	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_,
			int p_147416_5_)
	{

		return null;
	}
}
