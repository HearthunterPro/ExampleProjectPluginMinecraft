package mc.CushyPro.PluginName;

import java.io.File;
import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.event.Event;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityTracker;
import net.minecraft.server.v1_12_R1.EnumDifficulty;
import net.minecraft.server.v1_12_R1.EnumGamemode;
import net.minecraft.server.v1_12_R1.IDataManager;
import net.minecraft.server.v1_12_R1.IProgressUpdate;
import net.minecraft.server.v1_12_R1.IWorldAccess;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.ServerNBTManager;
import net.minecraft.server.v1_12_R1.WorldData;
import net.minecraft.server.v1_12_R1.WorldLoaderServer;
import net.minecraft.server.v1_12_R1.WorldManager;
import net.minecraft.server.v1_12_R1.WorldServer;
import net.minecraft.server.v1_12_R1.WorldSettings;
import net.minecraft.server.v1_12_R1.WorldType;

public class WorldGenThread {

	@SuppressWarnings("deprecation")
	public static void createWorld(WorldCreator creator) {
		CraftServer craftserver = (CraftServer) Bukkit.getServer();
		Validate.notNull(creator, "Creator may not be null");
		String name = creator.name();
		ChunkGenerator g = creator.generator();
		File folder = new File(craftserver.getWorldContainer(), name);
		World world = craftserver.getWorld(name);
		WorldType type = WorldType.getType(creator.type().getName());
		boolean generateStructures = creator.generateStructures();
		if (world != null)
			return;
		if (folder.exists() && !folder.isDirectory())
			throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
		if (g == null)
			g = craftserver.getGenerator(name);
		WorldLoaderServer worldLoaderServer = new WorldLoaderServer(craftserver.getWorldContainer(),
				craftserver.getHandle().getServer().dataConverterManager);
		if (worldLoaderServer.isConvertable(name)) {
			craftserver.getLogger().info("Converting world '" + name + "'");
			worldLoaderServer.convert(name, new IProgressUpdate() {
				private long b = System.currentTimeMillis();

				public void a(String s) {
				}

				public void a(int i) {
					if (System.currentTimeMillis() - this.b >= 1000L) {
						this.b = System.currentTimeMillis();
						MinecraftServer.LOGGER.info("Converting... " + i + "%");
					}
				}

				public void c(String s) {
				}
			});
		}
		final ChunkGenerator generator = g;
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					int dimension = 10 + craftserver.getServer().worlds.size();
					boolean used = false;
					while (true) {
						for (WorldServer server : craftserver.getServer().worlds) {
							used = (server.dimension == dimension);
							if (used) {
								dimension++;
								break;
							}
						}
						if (!used) {
							boolean hardcore = false;
							ServerNBTManager serverNBTManager = new ServerNBTManager(craftserver.getWorldContainer(),
									name, true, (craftserver.getHandle().getServer()).dataConverterManager);
							WorldData worlddata = serverNBTManager.getWorldData();
							WorldSettings worldSettings = null;
							if (worlddata == null) {
								worldSettings = new WorldSettings(creator.seed(),
										EnumGamemode.getById(craftserver.getDefaultGameMode().getValue()),
										generateStructures, hardcore, type);
								worldSettings.setGeneratorSettings(creator.generatorSettings());
								worlddata = new WorldData(worldSettings, name);
							}
							worlddata.checkName(name);
							WorldServer internal = (WorldServer) (new WorldServer(craftserver.getServer(),
									(IDataManager) serverNBTManager, worlddata, dimension,
									craftserver.getServer().methodProfiler, creator.environment(), generator)).b();
							if (!Check(craftserver, name.toLowerCase(Locale.ENGLISH)))
								return;
							if (worldSettings != null)
								internal.a(worldSettings);
							internal.scoreboard = craftserver.getScoreboardManager().getMainScoreboard().getHandle();
							internal.tracker = new EntityTracker(internal);
							internal.addIWorldAccess(
									(IWorldAccess) new WorldManager(craftserver.getServer(), internal));
							internal.worldData.setDifficulty(EnumDifficulty.EASY);
							internal.setSpawnFlags(true, true);
							craftserver.getServer().worlds.add(internal);
							craftserver.getPluginManager().callEvent((Event) new WorldInitEvent(internal.getWorld()));
							System.out.println(
									"Preparing start region for level " + (craftserver.getServer().worlds.size() - 1)
											+ " (Seed: " + internal.getSeed() + ")");
							if (internal.getWorld().getKeepSpawnInMemory()) {
								short short1 = 196;
								long i = System.currentTimeMillis();
								for (int j = -short1; j <= short1; j += 16) {
									for (int k = -short1; k <= short1; k += 16) {
										long l = System.currentTimeMillis();
										if (l < i)
											i = l;
										if (l > i + 1L) {
											int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
											int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;
											System.out.println(
													"Preparing spawn area for " + name + ", " + (j1 * 100 / i1) + "%");
											i = l;
										}
										BlockPosition chunkcoordinates = internal.getSpawn();
										final int a = j;
										final int b = k;
										new BukkitRunnable() {

											@Override
											public void run() {
												internal.getChunkProviderServer().getChunkAt(
														chunkcoordinates.getX() + a >> 4,
														chunkcoordinates.getZ() + b >> 4);
											}

										};
									}
									Thread.sleep(1000);
								}
							}
							craftserver.getPluginManager().callEvent((Event) new WorldLoadEvent(internal.getWorld()));
							return;
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.runTaskAsynchronously(Data.plugin);
	}

	private static boolean Check(CraftServer craftserver, String name) {
		for (World w : craftserver.getWorlds()) {
			if (w.getName().toLowerCase().equals(name)) {
				return true;
			}
		}
		return false;
	}
}