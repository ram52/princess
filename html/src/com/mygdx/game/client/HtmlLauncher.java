package com.mygdx.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.TimeUtils;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mygdx.core.MyGdxGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new MyGdxGame();
        }


        public Preloader.PreloaderCallback getPreloaderCallback () {
                final Panel preloaderPanel = new VerticalPanel();
                preloaderPanel.setStyleName("gdx-preloader");
                final Image logo = new Image("pleasewait.gif");
                logo.setStyleName("logo");
                preloaderPanel.add(logo);
                final Panel meterPanel = new SimplePanel();
//                meterPanel.setStyleName("gdx-meter");
//                meterPanel.addStyleName("red");
                final InlineHTML meter = new InlineHTML();
                final Style meterStyle = meter.getElement().getStyle();
                meterStyle.setWidth(0, Style.Unit.PCT);
                meterPanel.add(meter);
                preloaderPanel.add(meterPanel);
                getRootPanel().add(preloaderPanel);
                return new Preloader.PreloaderCallback() {

                        @Override
                        public void error (String file) {
                                System.out.println("error: " + file);
                        }

                        @Override
                        public void update (Preloader.PreloaderState state) {
                                meterStyle.setWidth(100f * state.getProgress(), Style.Unit.PCT);
                        }

                };
        }

//        @Override
//        public Preloader.PreloaderCallback getPreloaderCallback () {
//                final Canvas canvas = Canvas.createIfSupported();
//                canvas.setWidth("" + (int)(MyGdxGame.V_WIDTH * 0.7f) + "px");
//                canvas.setHeight("70px");
//                getRootPanel().add(canvas);
//                final Context2d context = canvas.getContext2d();
//                context.setTextAlign(Context2d.TextAlign.CENTER);
//                context.setTextBaseline(Context2d.TextBaseline.MIDDLE);
//                context.setFont("18pt Calibri");
//
//                return new Preloader.PreloaderCallback() {
////                        @Override
////                        public void done () {
////                                context.fillRect(0, 0, 300, 40);
////                        }
////
////                        @Override
////                        public void loaded (String file, int loaded, int total) {
////                                System.out.println("loaded " + file + "," + loaded + "/" + total);
////                                String color = Pixmap.make(30, 30, 30, 1);
////                                context.setFillStyle(color);
////                                context.setStrokeStyle(color);
////                                context.fillRect(0, 0, 300, 70);
////                                color = Pixmap.make(200, 200, 200, (((TimeUtils.nanoTime() - loadStart) % 1000000000) / 1000000000f));
////                                context.setFillStyle(color);
////                                context.setStrokeStyle(color);
////                                context.fillRect(0, 0, 300 * (loaded / (float)total) * 0.97f, 70);
////
////                                context.setFillStyle(Pixmap.make(50, 50, 50, 1));
////                                context.fillText("loading", 300 / 2, 70 / 2);
////                        }
//
//                        @Override
//                        public void update(Preloader.PreloaderState state) {
//                                Gdx.app.debug("HTMLLAUNCHER","progress="+state.getProgress());
//                                int loaded = (int) state.getProgress();
//                                int total = (int) state.getTotalSize();
//                                String color = "rgb(30, 30, 30)";
//                                context.setFillStyle(color);
//                                context.setStrokeStyle(color);
//                                context.fillRect(0, 0, 300, 70);
//                                color = "rgb(200, 200, 200)";;//Pixmap.make(200, 200, 200, (((TimeUtils.nanoTime() - loadStart) % 1000000000) / 1000000000f));
//                                context.setFillStyle(color);
//                                context.setStrokeStyle(color);
//                                context.fillRect(0, 0, 300 * (loaded / (float)total) * 0.97f, 70);
//
//                                context.setFillStyle("rgb(50, 50, 50)");
//                                context.fillText("loading", 300 / 2, 70 / 2);
//                        }
//
//                        @Override
//                        public void error (String file) {
//                                Gdx.app.debug("HTMLLAUNCHER","error="+file);
//                        }
//                };
//        }
}