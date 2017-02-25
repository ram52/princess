package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.mygdx.core.MyGdxGame.ConfirmInterface;
import com.mygdx.core.MyGdxGame.RequestHandler;
import com.mygdx.core.entities.ActionResolver;

public class RequestHandlerDesktop implements RequestHandler{
	private static String LOG_TAG = RequestHandlerDesktop.class.getSimpleName();

	@Override
	public void confirm(ConfirmInterface confirmInterface) {
		Gdx.app.debug(LOG_TAG, "Confirm");
	}

}
