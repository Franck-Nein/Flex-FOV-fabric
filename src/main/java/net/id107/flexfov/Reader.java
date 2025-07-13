package net.id107.flexfov;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class Reader {

	public static String read(String resourceIn) {
		ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
		Resource resource = null;
		InputStream is;
		try {
			resource = resourceManager.getResource(new Identifier("flex-fov", resourceIn)).get();
			is = resource.getInputStream();
		} catch (IOException e) {
			FlexFOV.LOGGER.error(e.getMessage());
			return "";
		}

		if (is == null) {
			System.out.println("Shader not found");
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			try {
				for (int i = is.read(); i != -1; i = is.read()) {
					sb.append((char) i);
				}
			} catch (IOException e) {
				FlexFOV.LOGGER.error(e.getMessage());
				return "";
			}

			return sb.toString();
		}
	}
}
