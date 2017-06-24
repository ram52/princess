package com.mygdx.game.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.core.MyGdxGame;

public class DesktopLauncher  {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = MyGdxGame.TITLE;
		config.width=MyGdxGame.V_WIDTH;
		config.height=MyGdxGame.V_HEIGHT;
		config.resizable = false;
		config.addIcon(MyGdxGame.desktopIconPath,FileType.Internal);
		new LwjglApplication(new MyGdxGame(new ActionResolverDesktop(),new RequestHandlerDesktop()), config);
	}
	

}
