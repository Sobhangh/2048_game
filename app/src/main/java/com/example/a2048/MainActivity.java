package com.example.a2048;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import android.database.DataSetObserver;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener {

    private GestureDetectorCompat mDetector;
    private   GridView bord;
    MyViewModel model;
    private TextView scoreView;
    private TextView highsocreView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = new ViewModelProvider(this).get(MyViewModel.class);
        bord=(GridView)findViewById(R.id.bord);
        scoreView = findViewById(R.id.score);
        highsocreView = findViewById(R.id.highScore);
        //if(savedInstanceState == null)
        highsocreView.setText(Integer.toString(model.getHighScore(this.getApplicationContext())));
        scoreView.setText(Integer.toString(model.getScore(this.getApplicationContext())));
        Log.i("bordactivity","reached");
        _2048adapter adapter = new _2048adapter(this,bord);
        bord.setAdapter(adapter);
        int [] b = model.getBord(this.getApplicationContext());
        if(b!=null) {
            adapter.setBord(b);
        }
        mDetector = new GestureDetectorCompat(this,this);
    }




    @Override
    public void onStop(){
        _2048adapter adapter = (_2048adapter) bord.getAdapter();
        super.onStop();
        model.setScore(this.getApplicationContext());
        model.setHighScore(this.getApplicationContext());
        model.setBord(adapter.getBord(),this.getApplicationContext());
    }

    public void Restart(View v){
        _2048adapter adapter = (_2048adapter) bord.getAdapter();
        //sethighscore should be before score in order to make sure the last highscore is saved
        //SHOULD WE CHANGE THE HIGHSCORE AS THE GAME GOES ON ALSO??
        model.setHighScore();
        model.setScore(0);
        scoreView.setText("0");
        adapter.generateNewBord();
    }

    private  boolean scrollStarted;
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);
        if(MotionEvent.ACTION_UP == action){
            scrollStarted = false;
        }
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float v, float v1) {
        if(!scrollStarted){
            scrollStarted = true;
            float deltaX = event1.getX()-event2.getX();
            float deltaY = event1.getY()-event2.getY();
            float tangent = abs(deltaY/deltaX);
            boolean moved = false;
            if(deltaX>0 && tangent<1 ) {
                /*left*/
                moved=left(bord);
            }
            else if(deltaX<0 && tangent<1){
                /*RIGHT*/
                moved=right(bord);
            }
            else if(deltaY<0 && tangent>1){
                /*Down*/
                moved=down(bord);
            }
            else if(deltaY>0 && tangent>1){
                /*up*/
                moved=up(bord);
            }
            if(moved) {
                ((_2048adapter) bord.getAdapter()).generaterandomposition();
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.whoosh);
                mp.start();
                if(((_2048adapter) bord.getAdapter()).lost()){
                    //TO DO: Lost fragment
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fragment_container_view, LooseFragment.class, null)
                            .addToBackStack(null)
                            .commit();

                    //Toast.makeText(this,"Lost",Toast.LENGTH_LONG).show();
                }
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    private void changeScore(GridView g,int position, int value){
        ((_2048adapter) g.getAdapter()).setItem(position,  value);
        model.addScore(value);
        scoreView.setText(Integer.toString(model.getScore()));
        if(value == 2048){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, WinFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private boolean left(GridView g){
        boolean moved=false;
        for(int i=0; i<13;i+=4){
            int [] seq = new int[4];
            int r=0;
            for(int j =0;j<4;j++){
                int p=i+j;
                int n = (int)g.getAdapter().getItem(p);
                if(n!=0){
                    seq[r]=n;
                    if(r!=j){
                        moved=true;
                    }
                    r++;
                }
            }
            int firstCouple = 0;
            while(firstCouple<3  ){
                if(seq[firstCouple]==seq[firstCouple+1] && seq[firstCouple]!=0){
                    seq[firstCouple]=2*seq[firstCouple+1];
                    seq[firstCouple+1]=0;
                    moved=true;
                    break;
                }
                firstCouple++;
            }
            int p=0;
            int j =0;
            while (p<4){
                if(seq[p]!=0){
                    //r[j]= seq[i];
                    if(p==firstCouple && p!= 3){
                        changeScore(g,j+i,seq[p]);
                    }
                    ((_2048adapter) g.getAdapter()).setItem(i+j, seq[p]);
                    j++;
                }
                p++;
            }
            int d = p-j;
            int q =0;
            while(q<d){
                //moved = true;
                ((_2048adapter) g.getAdapter()).setItem(i+q+j, 0);
                q++;
            }
        }
        return moved;
    }

    private boolean up(GridView g){
        boolean moved=false;
        for(int j =0;j<4;j++){
            int [] seq = new int[4];
            int r=0;
            for(int i=0; i<4;i++){
                int p=4*i+j;
                int n = (int)g.getAdapter().getItem(p);
                if(n!=0){
                    seq[r]=n;
                    if(r!=i){
                        moved=true;
                    }
                    r++;
                }
            }
            int firstCouple = 0;
            while(firstCouple<3  ){
                if(seq[firstCouple]==seq[firstCouple+1] && seq[firstCouple]!=0){
                    seq[firstCouple]=2*seq[firstCouple+1];
                    seq[firstCouple+1]=0;
                    moved=true;
                    break;
                }
                firstCouple++;
            }
            int p=0;
            int i =0;
            while (p<4){
                if(seq[p]!=0){
                    //r[j]= seq[i];
                    if(p==firstCouple && p!= 3){
                        changeScore(g,4*i+j,seq[p]);
                    }
                    ((_2048adapter) g.getAdapter()).setItem(4*i+j, seq[p]);
                    i++;
                }
                p++;
            }
            int d = p-i;
            int q =0;
            while(q<d){
                //moved = true;
                ((_2048adapter) g.getAdapter()).setItem(4*(i+q)+j, 0);
                q++;
            }
        }
        return moved;
    }

    private boolean down(GridView g){
        boolean moved=false;
        for(int j =0;j<4;j++){
            int [] seq = new int[4];
            int r=3;
            for(int i=3; i>=0;i--){
                int p=4*i+j;
                int n = (int)g.getAdapter().getItem(p);
                if(n!=0){
                    seq[r]=n;
                    if(r!=i){
                        moved=true;
                    }
                    r--;
                }
            }
            int firstCouple = 3;
            while(firstCouple>0  ){
                if(seq[firstCouple]==seq[firstCouple-1] && seq[firstCouple]!=0){
                    seq[firstCouple]=2*seq[firstCouple-1];
                    seq[firstCouple-1]=0;
                    moved=true;
                    break;
                }
                firstCouple--;
            }
            int p=3;
            int i =3;
            while (p>=0){
                if(seq[p]!=0){
                    //r[j]= seq[i];
                    if(p==firstCouple && p!= 0){
                        changeScore(g,4*i+j,seq[p]);
                    }
                    ((_2048adapter) g.getAdapter()).setItem(4*i+j, seq[p]);
                    i--;
                }
                p--;
            }
            int d = i-p;
            int q =0;
            while(q<d){
                //moved = true;
                ((_2048adapter) g.getAdapter()).setItem(4*(i-q)+j, 0);
                q++;
            }
        }
        return moved;
    }

    private boolean right(GridView g){
        boolean moved=false;
        for(int i=0; i<13;i+=4){
            int [] seq = new int[4];
            int r=3;
            for(int j =3;j>=0;j--){
                int p=i+j;
                int n = (int)g.getAdapter().getItem(p);
                if(n!=0){
                    seq[r]=n;
                    if(r!=j){
                        moved=true;
                    }
                    r--;
                }

            }
            int firstCouple = 3;
            while(firstCouple>0  ){
                if(seq[firstCouple]==seq[firstCouple-1] && seq[firstCouple]!=0){
                    seq[firstCouple]=2*seq[firstCouple-1];
                    seq[firstCouple-1]=0;
                    moved=true;
                    break;
                }
                firstCouple--;
            }
            int p=3;
            int j =3;
            while (p>=0){
                if(seq[p]!=0){
                    //r[j]= seq[i];
                    if(p==firstCouple && p!= 0){
                        changeScore(g,j+i,seq[p]);
                    }
                    ((_2048adapter) g.getAdapter()).setItem(i+j, seq[p]);
                    j--;
                }
                p--;
            }
            int d = j-p;
            int q =0;
            while(q<d){
                //moved = true;
                ((_2048adapter) g.getAdapter()).setItem(i-q+j, 0);
                q++;
            }
        }
        return moved;
    }
}
