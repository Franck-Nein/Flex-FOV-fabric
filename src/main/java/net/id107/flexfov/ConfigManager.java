package net.id107.flexfov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.fabricmc.loader.api.FabricLoader;
import net.id107.flexfov.gui.SettingsGui;
import net.id107.flexfov.gui.advanced.AdvancedGui;
import net.id107.flexfov.projection.Cylinder;
import net.id107.flexfov.projection.Equirectangular;
import net.id107.flexfov.projection.Fisheye;
import net.id107.flexfov.projection.Projection;

public class ConfigManager {

	private static final Path filePath = Paths.get(String.valueOf(FabricLoader.getInstance().getConfigDir()) + "/FlexFOVconfig.bin");
	
	public static void loadConfig() {
		if (Files.exists(filePath, new LinkOption[0])) {
			byte[] bytes;
			try {
				bytes = Files.readAllBytes(filePath);
				if (bytes.length < 17) return;
				if (bytes[0] != 4) return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			SettingsGui.currentGui = bytes[1];
			AdvancedGui.currentGui = bytes[2];
			Fisheye.fisheyeType = bytes[3];
			Projection.skyBackground = bytes[4] != 0;
			Projection.fov = ((bytes[5]&0xFF)<<8) | (bytes[6]&0xFF);
			Cylinder.fovy = ((bytes[7]&0xFF)<<8) | (bytes[8]&0xFF);
			Fisheye.fullFrame = bytes[9] != 0;
			Equirectangular.drawCircle = bytes[10] != 0;
			Equirectangular.stabilizePitch = bytes[11] != 0;
			Equirectangular.stabilizeYaw = bytes[12] != 0;
			Projection.zoom = (((bytes[13]<<8)&0xFF) | (bytes[14]&0xFF))/100f;
			if ((bytes[13] & 0b10000000) != 0) {
				Projection.zoom = -Projection.zoom;
			}
			Projection.showHand = bytes[15] != 0;
			Projection.defaultProjection = Projection.Projections.values()[bytes[16]];
		}

	}

	public static void saveConfig() {
		byte[] bytes = new byte[] {
				(byte)4,
				(byte)SettingsGui.currentGui,
				(byte)AdvancedGui.currentGui,
				(byte)Fisheye.fisheyeType,
				Projection.skyBackground ? (byte)1 : (byte)0,
				(byte)((Math.round(Projection.fov)>>8)&0xFF),
				(byte)(Math.round(Projection.fov)&0xFF),
				(byte)((Math.round(Cylinder.fovy)>>8)&0xFF),
				(byte)(Math.round(Cylinder.fovy)&0xFF),
				Fisheye.fullFrame ? (byte)1 : (byte)0,
				Equirectangular.drawCircle ? (byte)1 : (byte)0,
				Equirectangular.stabilizePitch ? (byte)1 : (byte)0,
				Equirectangular.stabilizeYaw ? (byte)1 : (byte)0,
				(byte)((Math.round(Projection.zoom*100)>>8)&0xFF),
				(byte)(Math.round(Projection.zoom*100)&0xFF),
				Projection.showHand ? (byte)1 : (byte)0,
				(byte)Projection.getProjection().ordinal() };

		try {
			Files.write(filePath, bytes, new OpenOption[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
