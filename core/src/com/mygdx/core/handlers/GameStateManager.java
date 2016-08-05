package com.mygdx.core.handlers;

import java.util.Stack;

import com.mygdx.core.MyGdxGame;
import com.mygdx.core.states.Credits;
import com.mygdx.core.states.GameOver;
import com.mygdx.core.states.GameState;
import com.mygdx.core.states.Menu;
import com.mygdx.core.states.Play;
import com.mygdx.core.states.Ending;
import com.mygdx.core.states.Shop;
import com.mygdx.core.states.Opening;
import com.mygdx.core.states.Tuto;

public class GameStateManager {

	private MyGdxGame game;
	private Stack<GameState> gameStates;
	public static final int MENU = 83774392;
	public static final int PLAY = 388031654;
	public static final int GAME_OVER = -9238732;
	public static final int TUTO = 5541654;
	public static final int CREDITS = 78826;
	public static final int ENDING = 21984251;
    public static final int STORY = 24576557;
	public static final int SHOP = 8424165;
	public boolean flag_menu, flag_play, flag_gameover, flag_tuto, flag_ending, flag_story, flag_credits, flag_shop;
	
	public GameStateManager( MyGdxGame game)
	{
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(STORY);
		flag_menu = false;
		flag_play = false;
		flag_gameover = false;
		flag_tuto = false;
        flag_ending = false;
        flag_story = true;
		flag_credits = false;
		flag_shop = false;
	}
	
	public MyGdxGame game() {return game;}
	
	public void update(float dt)
	{
		gameStates.peek().update(dt);
	}
	
	public void render()
	{
		gameStates.peek().render();
	}
	
	private GameState getState(int state)
	{
        if(state == STORY){
            flag_menu = false;
            flag_play = false;
            flag_gameover = false;
            flag_tuto = false;
            flag_ending = false;
            flag_story = true;
			flag_credits = false;
			flag_shop = false;
            return new Opening(this);
        }
        if(state == MENU){
            flag_menu = true;
            flag_play = false;
            flag_gameover = false;
            flag_tuto = false;
            flag_ending = false;
            flag_story = false;
			flag_credits = false;
			flag_shop = false;
			return new Menu(this);
        }
		if(state == PLAY){
			flag_menu = false;
			flag_play = true;
			flag_gameover = false;
            flag_tuto = false;
            flag_ending = false;
            flag_story = false;
			flag_credits = false;
			flag_shop = false;
			return new Play(this);
			}
		if(state == GAME_OVER){
			flag_menu = false;
			flag_play = false;
			flag_gameover = true;
            flag_tuto = false;
            flag_ending = false;
            flag_story = false;
			flag_credits = false;
			flag_shop = false;
			return new GameOver(this);
			}
		if(state == TUTO){
			flag_tuto = true;
			flag_menu = false;
			flag_play = false;
			flag_gameover = false;
            flag_ending = false;
            flag_story = false;
			flag_credits = false;
			flag_shop = false;
			return new Tuto(this);
			}

        if(state == ENDING){
            flag_tuto = false;
            flag_menu = false;
            flag_play = false;
            flag_gameover = false;
            flag_ending = true;
            flag_story = false;
			flag_credits = false;
			flag_shop = false;
            return new Ending(this);
        }

		if(state == CREDITS){
			flag_tuto = false;
			flag_menu = false;
			flag_play = false;
			flag_gameover = false;
			flag_ending = false;
			flag_story = false;
			flag_credits = true;
			flag_shop = false;
			return new Credits(this);
		}

		if(state == SHOP){
			flag_tuto = false;
			flag_menu = false;
			flag_play = false;
			flag_gameover = false;
			flag_ending = false;
			flag_story = false;
			flag_credits = true;
			flag_shop = false;
			return new Shop(this);
		}
		return null;
	}
	
	public void setState(int state)
	{
		if(!gameStates.isEmpty()){
		popState();
		pushState(state);
		}
	}
	
	public void pushState(int state)
	{
		gameStates.push(getState(state));
	}
	
	public void popState()
	{
		GameState g = gameStates.pop();
		g.dispose();
	}
	
	public Stack<GameState> getgameStates(){
		return gameStates;
	}
	
	
}
