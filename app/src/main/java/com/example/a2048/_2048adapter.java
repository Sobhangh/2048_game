package com.example.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.example.a2048.BorderedTextView;
import java.util.ArrayList;
import java.util.Random;

public class _2048adapter extends BaseAdapter {
    Context c;
    int[] num=new int[16];
    GridView g;


    public _2048adapter(Context c,GridView gr){
        for(int i=0;i<16;i++){
            num[i]=0;
        }
        Random r=new Random();
        int random1 = r.nextInt(16);
        int random2 = r.nextInt(16);
        while(random1==random2){
            random2 = r.nextInt(16);
        }
        num[random1]=2;
        num[random2]=2;
        this.c=c;
        this.g=gr;
    }
    @Override
    public int getCount() {
        return 16;
    }

    @Override
    public Object getItem(int i) {
        return num[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    protected void setItem(int position,int value){
        num[position]=value;
        setvalue(value,(BorderedTextView) g.getChildAt(position));

    }

    protected void generateNewBord(){
        for(int i=0;i<16;i++){
            setItem(i,0);
        }
        Random r=new Random();
        int random1 = r.nextInt(16);
        int random2 = r.nextInt(16);
        while(random1==random2){
            random2 = r.nextInt(16);
        }
        setItem(random1,2);
        setItem(random2,2);
    }

    public boolean lost(){
        for(int i=0;i<16;i++){
            if(num[i]==0){
                return false;
            }
        }
        for(int i =0 ; i<16 ; i+=4){
            for(int j =0 ; j<3;j++){
                if(num[i+j] == num[i+j+1]){
                    return false;
                }
            }
        }
        for(int j =0 ; j<4 ; j++){
            for(int i =0 ; i<12;i+=4){
                if(num[i+j] == num[i+j+4]){
                    return false;
                }
            }
        }
        return true;
    }

    protected void setBord(int [] bord){
        for(int i=0;i<bord.length;i++) {
            num[i]=bord[i];
        }
    }

    public int[] getBord() {
        return num;
    }

    public void generaterandomposition(){
        ArrayList<Integer> a =new ArrayList<Integer>();
        for(int i=0;i<16;i++){
            if(num[i]==0){
                a.add(i);
            }
        }
        Random r=new Random();
        int random1 = r.nextInt(a.size());
        setItem(a.get(random1),2);
    }

    private void setvalue(int value,BorderedTextView v){
        if(value!=0) {
            String[] allColors = c.getResources().getStringArray(R.array.colors);
            v.setText(Integer.toString(value));
            v.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            v.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            Log.i("COLOOR",Integer.toString((int)(Math.log(value)/ Math.log(2))-1));
            v.setColor(Color.parseColor(allColors[Math.min(11,(int)(Math.log(value)/ Math.log(2))-1)]));
            /**
            v.setBackground(v.getContext().getDrawable(R.drawable.border));
            v.setBackgroundColor(Color.parseColor(allColors[(int)(Math.log(value)/ Math.log(2))-1]));**/

        }
        else{
            v.setText("");
            v.setColor(c.getResources().getColor(R.color.bordbackColor));
            //v.setBackgroundColor(c.getResources().getColor(R.color.bordbackColor));
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.i("getview","reached");
        BorderedTextView v=new BorderedTextView(this.c);
        //v.setBackgroundColor(Color.parseColor("#00000000"));
        String[] allColors = c.getResources().getStringArray(R.array.colors);
        int w=g.getWidth();
        int h=g.getHeight();
        if(view==null ){
            if(num[i]!=0) {
                v.setColor(Color.parseColor(allColors[Math.min(11,(int)(Math.log(num[i])/ Math.log(2))-1)]));
                v.setText(Integer.toString(num[i]));
                v.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                v.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);

                /**
                v.setBackgroundColor(Color.parseColor(allColors[(int)(Math.log(num[i])/ Math.log(2))-1]));
                v.setBackground(v.getContext().getDrawable(R.drawable.border));**/

            }
            else{
                v.setColor(c.getResources().getColor(R.color.bordbackColor));
                //v.setBackgroundColor(c.getResources().getColor(R.color.bordbackColor));
            }

            v.setWidth(w/4);
            v.setHeight(h/4);
            Log.i("getview",Integer.toString(num[i]));
        }
        else{
            Log.i("getviewnuuuuul",Integer.toString(i));
            v=(BorderedTextView) view;
            v.setWidth(w/4);
            v.setHeight(h/4);
            if(num[i]!=0) {
                v.setColor(Color.parseColor(allColors[Math.min(11,(int)(Math.log(num[i])/ Math.log(2))-1)]));
                //v.setBackgroundColor(Color.parseColor(allColors[(int)(Math.log(num[i])/ Math.log(2))-1]));
            }
            else{
                v.setColor(c.getResources().getColor(R.color.bordbackColor));
                //v.setBackgroundColor(c.getResources().getColor(R.color.bordbackColor));
            }
        }
        return v;
    }
}
