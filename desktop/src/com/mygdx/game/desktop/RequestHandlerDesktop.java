package com.mygdx.game.desktop;

import com.mygdx.core.MyGdxGame.ConfirmInterface;
import com.mygdx.core.MyGdxGame.RequestHandler;
import com.mygdx.core.entities.ActionResolver;

public class RequestHandlerDesktop implements RequestHandler{

	@Override
	public void confirm(ConfirmInterface confirmInterface) {
		System.out.println("Confirm");		
	}

}
