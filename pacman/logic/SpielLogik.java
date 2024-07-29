package com.example.pacman.logic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.pacman.R;
import com.example.pacman.activities.GameOverActivity;
import com.example.pacman.activities.PauseActivity;

public class SpielLogik extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private static MediaPlayer player;
    private Thread thread;
    private final SurfaceHolder holder;
    private boolean kannZeichnen = true;

    private final Paint paint;
    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp;
    private Bitmap pauseBitmap;
    private Bitmap ghostBitmap;
    private final int totalFrame = 4;
    private int currentPacmanFrame = 0;
    private long frameTicker;
    private int xPosPacman;
    private int yPosPacman;
    private int xPosGhost;
    private int yPosGhost;
    int xDistance;
    int yDistance;
    private float x1, x2, y1, y2;           // Enderichtung Wischen
    private int richtungWischen = 4;
    private int naechsteRichtung = 4;
    private int pacmanGesichtRichtung = 0;
    private int geistRichtung;
    private final int screenWidth;
    private final int screenHeight;

    private int blockGroesse;
    public static int LONG_PRESS_TIME = 800;  // Time in milliseconds
    private int aktuellerPunktstand = 0;
    final Handler handler = new Handler();

    public SpielLogik(Context context) {
        super(context);
        player = MediaPlayer.create(this.getContext(), R.raw.pacman_beginning);
        player.setVolume(100, 100);
        player.start();
        holder = getHolder();
        holder.addCallback(this);
        frameTicker = 1000/totalFrame;

        paint = new Paint();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        blockGroesse = screenWidth/18;
        blockGroesse = (blockGroesse / 5) * 5;
        xPosGhost = 8 * blockGroesse;
        geistRichtung = 4;
        yPosGhost = 10 * blockGroesse;
        xPosPacman = 8 * blockGroesse;
        yPosPacman = 15 * blockGroesse;

        loadBitmapImages();
    }

    @Override
    public void run() {
        while (kannZeichnen) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                mapZeichnen(canvas);
                updateFrame(System.currentTimeMillis());
                geistBewegen(canvas);
                pacmanBewegen(canvas);
                kugelZeichnen(canvas);
                punkteUpdate(canvas);
                pauseButtonZeichnen(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
    public void geistBewegen(Canvas canvas) {
        short nummer;
        xDistance = xPosPacman - xPosGhost;
        yDistance = yPosPacman - yPosGhost;

        if ((xPosGhost % blockGroesse == 0) && (yPosGhost % blockGroesse == 0)) {
            nummer = map[yPosGhost / blockGroesse][xPosGhost / blockGroesse];

            // durch Tunnel
            if (xPosGhost >= blockGroesse * 18) {
                xPosGhost = 0;
            }
            if (xPosGhost < 0) {
                xPosGhost = blockGroesse * 18;
            }

            // rechts und runter
            if (xDistance >= 0 && yDistance >= 0) {
                if ((nummer & 4) == 0 && (nummer & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        geistRichtung = 1;
                    } else {
                        geistRichtung = 2;
                    }
                }
                else if ((nummer & 4) == 0) {
                    geistRichtung = 1;
                }
                else if ((nummer & 8) == 0) {
                    geistRichtung = 2;
                }
                else
                    geistRichtung = 3;
            }
            // rechts und hoch
            if (xDistance >= 0 && yDistance <= 0) {
                if ((nummer & 4) == 0 && (nummer & 2) == 0 ) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        geistRichtung = 1;
                    } else {
                        geistRichtung = 0;
                    }
                }
                else if ((nummer & 4) == 0) {
                    geistRichtung = 1;
                }
                else if ((nummer & 2) == 0) {
                    geistRichtung = 0;
                }
                else geistRichtung = 2;
            }
            // links und runter
            if (xDistance <= 0 && yDistance >= 0) {
                if ((nummer & 1) == 0 && (nummer & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        geistRichtung = 3;
                    } else {
                        geistRichtung = 2;
                    }
                }
                else if ((nummer & 1) == 0) {
                    geistRichtung = 3;
                }
                else if ((nummer & 8) == 0) {
                    geistRichtung = 2;
                }
                else geistRichtung = 1;
            }
            // links und oben
            if (xDistance <= 0 && yDistance <= 0) {
                if ((nummer & 1) == 0 && (nummer & 2) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        geistRichtung = 3;
                    } else {
                        geistRichtung = 0;
                    }
                }
                else if ((nummer & 1) == 0) {
                    geistRichtung = 3;
                }
                else if ((nummer & 2) == 0) {
                    geistRichtung = 0;
                }
                else geistRichtung = 2;
            }

            // gucken, ob Wand da ist
            if ((geistRichtung == 3 && (nummer & 1) != 0) ||
                (geistRichtung == 1 && (nummer & 4) != 0) ||
                (geistRichtung == 0 && (nummer & 2) != 0) ||
                (geistRichtung == 2 && (nummer & 8) != 0) ) {
                geistRichtung = 4;
            }
        }


        if (geistRichtung == 0) {
            yPosGhost += -blockGroesse / 20;
        } else if (geistRichtung == 1) {
            xPosGhost += blockGroesse / 20;
        } else if (geistRichtung == 2) {
            yPosGhost += blockGroesse / 20;
        } else if (geistRichtung == 3) {
            xPosGhost += -blockGroesse / 20;
        }

        canvas.drawBitmap(ghostBitmap, xPosGhost, yPosGhost, paint);
    }
    public void pacmanBewegen(Canvas canvas) {
        short nummer;
        if ( (xPosPacman % blockGroesse == 0) && (yPosPacman  % blockGroesse == 0) ) {
            if (xPosPacman >= blockGroesse * 18) {
                // Pacman geht durch rechte Tunnel zu linker
                xPosPacman = 0;
            }
            nummer = map[yPosPacman / blockGroesse][xPosPacman / blockGroesse];
            if ((nummer & 16) != 0) {
                player = MediaPlayer.create(this.getContext(), R.raw.pacman_chomp);
                player.setVolume(100, 100);
                player.start();
                map[yPosPacman / blockGroesse][xPosPacman / blockGroesse] = (short) (nummer ^ 16);
                aktuellerPunktstand += 10;
            }

            // Richtungspufferung
            if (!((naechsteRichtung == 3 && (nummer & 1) != 0) ||
                    (naechsteRichtung == 1 && (nummer & 4) != 0) ||
                    (naechsteRichtung == 0 && (nummer & 2) != 0) ||
                    (naechsteRichtung == 2 && (nummer & 8) != 0))) {
                pacmanGesichtRichtung = richtungWischen = naechsteRichtung;
            }

            // gucken, ob da eine Wand ist
            if ((richtungWischen == 3 && (nummer & 1) != 0) ||
                    (richtungWischen == 1 && (nummer & 4) != 0) ||
                    (richtungWischen == 0 && (nummer & 2) != 0) ||
                    (richtungWischen == 2 && (nummer & 8) != 0)) {
                richtungWischen = 4;
            }
        }

        if (xPosPacman < 0) {
            // Pacmangeht durch linke Tunnel zu rechter
            xPosPacman = blockGroesse * 18;
        }
        pacmanZeichnen(canvas);

        if (richtungWischen == 0) {
            yPosPacman += -blockGroesse /15;
        } else if (richtungWischen == 1) {
            xPosPacman += blockGroesse /15;
        } else if (richtungWischen == 2) {
            yPosPacman += blockGroesse /15;
        } else if (richtungWischen == 3) {
            xPosPacman += -blockGroesse /15;
        }
        endeSpiel();
    }

    private void endeSpiel(){
        if (xPosPacman >= xPosGhost-blockGroesse/1.5 && xPosPacman <= xPosGhost+blockGroesse/1.5 && yPosPacman == yPosGhost ||
                yPosPacman >= yPosGhost-blockGroesse/1.5 && yPosPacman <= yPosGhost+blockGroesse/1.5 && xPosPacman == xPosGhost)
        {
            player = MediaPlayer.create(this.getContext(), R.raw.pacman_death);
            player.setVolume(100, 100);
            player.start();
            Intent endeIntent = new Intent(getContext(), GameOverActivity.class);
            endeIntent.putExtra("intValue", aktuellerPunktstand);
            getContext().startActivity(endeIntent);
        }

        boolean kugelLeer = true;
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 18; j++) {
                if ((map[i][j] & 16) != 0) {
                    kugelLeer = false;
                    break;
                }
            }
            if(!kugelLeer){
                break;
            }
        }
        if (kugelLeer) {
            player = MediaPlayer.create(this.getContext(), R.raw.pacman_win);
            player.setVolume(100, 100);
            player.start();
            Intent endeIntent = new Intent(getContext(), GameOverActivity.class);
            endeIntent.putExtra("intValue", aktuellerPunktstand);
            getContext().startActivity(endeIntent);
        }
    }

    public void pacmanZeichnen(Canvas canvas) {
        switch (pacmanGesichtRichtung) {
            case (0):
                canvas.drawBitmap(pacmanUp[currentPacmanFrame], xPosPacman, yPosPacman, paint);
                break;
            case (1):
                canvas.drawBitmap(pacmanRight[currentPacmanFrame], xPosPacman, yPosPacman, paint);
                break;
            case (2):
                canvas.drawBitmap(pacmanDown[currentPacmanFrame], xPosPacman, yPosPacman, paint);
                break;
            default:
                canvas.drawBitmap(pacmanLeft[currentPacmanFrame], xPosPacman, yPosPacman, paint);
                break;
        }
    }

    public void kugelZeichnen(Canvas canvas) {
        float x;
        float y;
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 18; j++) {
                x = j * blockGroesse;
                y = i * blockGroesse;
                if ((map[i][j] & 16) != 0)
                    canvas.drawCircle(x + blockGroesse / 2, y + blockGroesse / 2, blockGroesse / 9, paint);
            }
        }
    }

    public void mapZeichnen(Canvas canvas) {
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3.0f);
        int x;
        int y;
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 18; j++) {
                x = j * blockGroesse;
                y = i * blockGroesse;
                if ((map[i][j] & 1) != 0) // draws left
                    canvas.drawLine(x, y, x, y + blockGroesse - 1, paint);

                if ((map[i][j] & 2) != 0) // draws top
                    canvas.drawLine(x, y, x + blockGroesse - 1, y, paint);

                if ((map[i][j] & 4) != 0) // draws right
                    canvas.drawLine(
                            x + blockGroesse, y, x + blockGroesse, y + blockGroesse - 1, paint);
                if ((map[i][j] & 8) != 0) // draws bottom
                    canvas.drawLine(
                            x, y + blockGroesse, x + blockGroesse - 1, y + blockGroesse, paint);
            }
        }
        paint.setColor(Color.WHITE);
    }
    public void pauseButtonZeichnen(Canvas canvas){
        paint.setTextSize(blockGroesse);
        canvas.drawBitmap(pauseBitmap,screenWidth-290,screenHeight-200,paint);
        paint.setTypeface(Typeface.create("joystick", Typeface.NORMAL));
        canvas.drawText("Long press to pause",screenWidth/2-50,screenHeight-150,paint);
    }

    public void punkteUpdate(Canvas canvas) {
        paint.setTextSize(blockGroesse*2);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create("joystick", Typeface.BOLD));
        String formattedScore = String.format("%05d", aktuellerPunktstand);
        String score = formattedScore;
        canvas.drawText(score, screenWidth/2, 30 * blockGroesse, paint);
    }

    Runnable longPressed = new Runnable() {
        public void run() {
            Intent pauseIntent = new Intent(getContext(), PauseActivity.class);
            getContext().startActivity(pauseIntent);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN): {
                x1 = event.getX();
                y1 = event.getY();
                handler.postDelayed(longPressed, LONG_PRESS_TIME);
                break;
            }
            case (MotionEvent.ACTION_UP): {
                x2 = event.getX();
                y2 = event.getY();
                wischenRichtungRechner();
                handler.removeCallbacks(longPressed);
                break;
            }
        }
        return true;
    }

    private void wischenRichtungRechner() {
        float xDifferenz = (x2 - x1);
        float yDifferenz = (y2 - y1);
        if (Math.abs(yDifferenz) > Math.abs(xDifferenz)) {
            if (yDifferenz < 0) {
                naechsteRichtung = 0;
            } else if (yDifferenz > 0) {
                naechsteRichtung = 2;
            }
        } else {
            if (xDifferenz < 0) {
                naechsteRichtung = 3;
            } else if (xDifferenz > 0) {
                naechsteRichtung = 1;
            }
        }
    }
    private void updateFrame(long gameTime) {
        if (gameTime > frameTicker + (totalFrame * 30)) {
            frameTicker = gameTime;
            currentPacmanFrame++;
            if (currentPacmanFrame >= totalFrame) {
                currentPacmanFrame = 0;
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("info", "Surface Created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("info", "Surface Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("info", "Surface Destroyed");
    }

    public void pause() {
        Log.i("info", "pause");
        kannZeichnen = false;
        thread = null;
    }

    public void resume() {
        Log.i("info", "resume");
        if (thread != null) {
            thread.start();
        }
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
            Log.i("info", "resume thread");
        }
        kannZeichnen = true;
    }

    private void loadBitmapImages() {
        int bildGroesse = screenWidth/18;
        bildGroesse = (bildGroesse/5)*5;

        pauseBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),R.drawable.pause), blockGroesse, blockGroesse,false);
        pacmanRight = new Bitmap[totalFrame];
        pacmanRight[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(),R.drawable.pacman_right2), bildGroesse, bildGroesse, false);
        pacmanRight[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right3), bildGroesse, bildGroesse, false);
        pacmanRight[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right4), bildGroesse, bildGroesse, false);
        pacmanRight[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right1), bildGroesse, bildGroesse, false);
        pacmanDown = new Bitmap[totalFrame];
        pacmanDown[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down2), bildGroesse, bildGroesse, false);
        pacmanDown[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down3), bildGroesse, bildGroesse, false);
        pacmanDown[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down4), bildGroesse, bildGroesse, false);
        pacmanDown[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down1), bildGroesse, bildGroesse, false);
        pacmanLeft = new Bitmap[totalFrame];
        pacmanLeft[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left2), bildGroesse, bildGroesse, false);
        pacmanLeft[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left3), bildGroesse, bildGroesse, false);
        pacmanLeft[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left4), bildGroesse, bildGroesse, false);
        pacmanLeft[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left1), bildGroesse, bildGroesse, false);
        pacmanUp = new Bitmap[totalFrame];
        pacmanUp[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up2), bildGroesse, bildGroesse, false);
        pacmanUp[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up3), bildGroesse, bildGroesse, false);
        pacmanUp[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up4), bildGroesse, bildGroesse, false);
        pacmanUp[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up1), bildGroesse, bildGroesse, false);

        ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.geist), bildGroesse, bildGroesse,false);
    }

    final short map[][] = new short[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {19, 26, 18, 26, 26, 26, 18, 26, 26, 26, 18, 26, 26, 26, 26, 26, 26, 22},
            {21, 0, 21, 0, 0, 0, 21, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 21},
            {17, 26, 16, 22, 0, 19, 20, 0, 0, 0, 17, 26, 26, 26, 26, 26, 26, 20},
            {21, 0, 17, 24, 26, 24, 16, 26, 26, 26, 20, 0, 0, 0, 0, 0, 0, 21},
            {21, 0, 21, 0, 0, 0, 21, 0, 0, 0, 17, 26, 26, 26, 26, 26, 26, 20},
            {17, 26, 20, 0, 0, 19, 24, 18, 26, 26, 20, 0, 0, 0, 0, 0, 0, 21},
            {21, 0, 25, 26, 26, 20, 0, 21, 0, 0, 17, 26, 18, 26, 26, 26, 26, 20},
            {21, 0, 0, 0, 0, 21, 0, 21, 0, 0, 21, 0, 21, 0, 0, 0, 0, 21},
            {17, 26, 26, 26, 26, 16, 26, 24, 26, 18, 24, 26, 16, 26, 26, 26, 26, 20},
            {21, 0, 0, 0, 0, 21, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 21},
            {21, 0, 0, 0, 0, 21, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 21},
            {17, 18, 18, 26, 26, 24, 26, 18, 26, 24, 18, 26, 24, 26, 26, 18, 18, 28},
            {26, 16, 20, 0, 0, 0, 0, 21, 0, 0, 21, 0, 0, 0, 0, 17, 16, 26},
            {19, 24, 24, 26, 26, 26, 18, 24, 10, 26, 24, 18, 26, 26, 26, 24, 24, 22}, // pacman fängt bei 10 an
            {21, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 21},
            {17, 26, 18, 26, 18, 26, 24, 22, 0, 0, 19, 24, 26, 18, 26, 18, 26, 20},
            {21, 0, 21, 0, 21, 0, 0, 21, 0, 0, 21, 0, 0, 21, 0, 21, 0, 21},
            {21, 0, 25, 18, 24, 26, 26, 24, 18, 18, 24, 26, 26, 24, 18, 28, 0, 21},
            {21, 0, 0, 21, 0, 0, 0, 0, 17, 20, 0, 0, 0, 0, 21, 0, 0, 21},
            {17, 26, 26, 24, 18, 26, 18, 26, 24, 24, 26, 18, 26, 18, 24, 26, 26, 20},
            {21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21},
            {21, 0, 19, 26, 20, 0, 17, 26, 26, 26, 26, 20, 0, 17, 26, 22, 0, 21},
            {25, 18, 20, 0, 21, 0, 21, 0, 0, 0, 0, 21, 0, 21, 0, 17, 18, 28},
            {26, 16, 28, 0, 21, 0, 21, 0, 0, 0, 0, 21, 0, 21, 0, 25, 16, 26},
            {0, 21, 0, 0, 17, 18, 24, 18, 26, 26, 18, 24, 18, 20, 0, 0, 21, 0},
            {0, 25, 26, 26, 24, 28, 0, 29, 0, 0, 29, 0, 25, 24, 26, 26, 28, 0}
    };


}