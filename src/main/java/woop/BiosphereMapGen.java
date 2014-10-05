package woop;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;

public class BiosphereMapGen extends MapGenBase
{
	BiosphereChunkProvider generator = null;

	public void func_151539_a(IChunkProvider chunkProvider, World world, int x, int z, Block[] blocks)
	{
		if (this.generator == null && chunkProvider instanceof BiosphereChunkProvider)
		{
			this.generator = (BiosphereChunkProvider)chunkProvider;
		}

		super.func_151539_a(chunkProvider, world, x, z, blocks);
	}

	protected void a(int i, int j, Block[] blocks, double d, double d1, double d2)
	{
		this.a(i, j, blocks, d, d1, d2, 10.0F + this.rand.nextFloat() * 20.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}

	protected void a(int chunkX, int chunkZ, Block[] blocks, double d, double d1, double d2, float f, float f1,
			float f2, int k, int l, double d3)
	{
		SphereChunk chunk = null;

		if (this.generator != null)
		{
			this.generator.setNoise(chunkX, chunkZ);
			chunk = this.generator.GetSphereChunk(chunkX, chunkZ);
		}

		double ccx = (double)(chunkX * 16 + 8);
		double ccz = (double)(chunkZ * 16 + 8);
		float f3 = 0.0F;
		float f4 = 0.0F;
		Random random = new Random(this.rand.nextLong());

		if (l <= 0)
		{
			int flag = this.range * 16 - 16;
			l = flag - random.nextInt(flag / 4);
		}

		boolean var61 = false;

		if (k == -1)
		{
			k = l / 2;
			var61 = true;
		}

		int j1 = random.nextInt(l / 2) + l / 4;

		for (boolean flag1 = random.nextInt(6) == 0; k < l; ++k)
		{
			double d6 = 1.5D + (double)(MathHelper.sin((float)k * (float)Math.PI / (float)l) * f * 1.0F);
			double d7 = d6 * d3;
			float f5 = MathHelper.cos(f2);
			float f6 = MathHelper.sin(f2);
			d += (double)(MathHelper.cos(f1) * f5);
			d1 += (double)f6;
			d2 += (double)(MathHelper.sin(f1) * f5);

			if (flag1)
			{
				f2 *= 0.92F;
			}
			else
			{
				f2 *= 0.7F;
			}

			f2 += f4 * 0.1F;
			f1 += f3 * 0.1F;
			f4 *= 0.9F;
			f3 *= 0.75F;
			f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

			if (!var61 && k == j1 && f > 1.0F)
			{
				this.a(
					chunkX,
					chunkZ,
					blocks,
					d,
					d1,
					d2,
					random.nextFloat() * 0.5F + 0.5F,
					f1 - ((float)Math.PI / 2F),
					f2 / 3.0F,
					k,
					l,
					1.0D);
				this.a(
					chunkX,
					chunkZ,
					blocks,
					d,
					d1,
					d2,
					random.nextFloat() * 0.5F + 0.5F,
					f1 + ((float)Math.PI / 2F),
					f2 / 3.0F,
					k,
					l,
					1.0D);
				return;
			}

			if (var61 || random.nextInt(4) != 0)
			{
				double d8 = d - ccx;
				double d9 = d2 - ccz;
				double d10 = (double)(l - k);
				double d11 = (double)(f + 2.0F + 16.0F);

				if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) { return; }

				if (d >= ccx - 16.0D - d6 * 2.0D && d2 >= ccz - 16.0D - d6 * 2.0D && d <= ccx + 16.0D + d6 * 2.0D
						&& d2 <= ccz + 16.0D + d6 * 2.0D)
				{
					int k1 = MathHelper.truncateDoubleToInt(d - d6) - chunkX * 16 - 1;
					int l1 = MathHelper.truncateDoubleToInt(d + d6) - chunkX * 16 + 1;
					int i2 = MathHelper.truncateDoubleToInt(d1 - d7) - 1;
					int j2 = MathHelper.truncateDoubleToInt(d1 + d7) + 1;
					int k2 = MathHelper.truncateDoubleToInt(d2 - d6) - chunkZ * 16 - 1;
					int l2 = MathHelper.truncateDoubleToInt(d2 + d6) - chunkZ * 16 + 1;

					if (k1 < 0)
					{
						k1 = 0;
					}

					if (l1 > 16)
					{
						l1 = 16;
					}

					if (i2 < 1)
					{
						i2 = 1;
					}

					if (j2 > 120)
					{
						j2 = 120;
					}

					if (k2 < 0)
					{
						k2 = 0;
					}

					if (l2 > 16)
					{
						l2 = 16;
					}

					boolean flag2 = false;
					int j3;
					int l3;

					for (l3 = k1; !flag2 && l3 < l1; ++l3)
					{
						for (int d12 = k2; !flag2 && d12 < l2; ++d12)
						{
							for (int j4 = j2 + 1; !flag2 && j4 >= i2 - 1; --j4)
							{
								j3 = (l3 * 16 + d12) * 128 + j4;

								if (j4 >= 0 && j4 < 128)
								{
									if (blocks[j3] == Blocks.flowing_water || blocks[j3] == Blocks.water
											|| blocks[j3] == Blocks.flowing_lava || blocks[j3] == Blocks.lava)
									{
										flag2 = true;
									}

									if (j4 != i2 - 1 && l3 != k1 && l3 != l1 - 1 && d12 != k2 && d12 != l2 - 1)
									{
										j4 = i2;
									}
								}
							}
						}
					}

					if (!flag2)
					{
						for (l3 = k1; l3 < l1; ++l3)
						{
							double var62 = ((double)(l3 + chunkX * 16) + 0.5D - d) / d6;

							for (j3 = k2; j3 < l2; ++j3)
							{
								int midY = chunk.getSurfaceLevel(l3, j3);
								double d13 = ((double)(j3 + chunkZ * 16) + 0.5D - d2) / d6;
								int k4 = (l3 * 16 + j3) * 128 + j2;

								for (int l4 = j2 - 1; l4 >= i2; --l4)
								{
									double d14 = ((double)l4 + 0.5D - d1) / d7;

									if (d14 > -0.7D && var62 * var62 + d14 * d14 + d13 * d13 < 1.0D)
									{
										Block block = blocks[k4];

										if (block == Blocks.stone || block == Blocks.sand || block == Blocks.gravel
												|| block == Blocks.diamond_ore || block == Blocks.lapis_ore
												|| block == Blocks.emerald_ore)
										{
											if (l4 < ModConsts.LAVA_LEVEL)
											{
												if (this.generator != null)
												{
													double d15 = chunk.getMainDistance(
														(int)Math.round(ccx + (double)l3 - 8.0D),
														l4 - 1,
														(int)Math.round(ccz + (double)j3 - 8.0D));

													if (d15 >= chunk.radius && d15 < chunk.radius + 5.0D)
													{
														blocks[k4] = Blocks.obsidian;
													}
													else if (d15 < chunk.radius)
													{
														blocks[k4] = Blocks.flowing_lava;
													}
												}
												else
												{
													blocks[k4] = Blocks.flowing_lava;
												}
											}
											else if (l4 < midY - 2 || l4 > midY - 1)
											{
												blocks[k4] = Blocks.air;
											}
										}
									}

									--k4;
								}
							}
						}

						if (var61)
						{
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Recursively called by generate() (generate) and optionally by itself.
	 */
	protected void recursiveGenerate(World world, int chunkX, int chunkZ, int k, int l, Block[] blocks)
	{
		int i1 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);

		if (this.rand.nextInt(5) != 0)
		{
			i1 = 0;
		}

		for (int j1 = 0; j1 < i1; ++j1)
		{
			double x = (double)(chunkX * 16 + this.rand.nextInt(16));
			double y = (double)this.rand.nextInt(ModConsts.WORLD_HEIGHT);
			double z = (double)(chunkZ * 16 + this.rand.nextInt(16));
			int k1 = 1;

			if (this.rand.nextInt(4) == 0)
			{
				this.a(k, l, blocks, x, y, z);
				k1 += this.rand.nextInt(4);
			}

			for (int l1 = 0; l1 < k1; ++l1)
			{
				float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
				this.a(k, l, blocks, x, y, z, f2 * 5.0F, f, f1, 0, 0, 0.5D);
			}
		}
	}
}
