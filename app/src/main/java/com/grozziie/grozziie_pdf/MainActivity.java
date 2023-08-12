//package com.example.administrator.test;
//
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.ColorDrawable;
//import android.media.Image;
//import android.provider.ContactsContract;
////import android.support.v7.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.animation.RotateAnimation;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class MainActivity extends AppCompatActivity {
//    private Button button;
//    private Button maxButton;
//    private Button minButton;
//    private Button removeButton;
//    private Button rotateButton;
//    private Button graffiti;
//    private PopupWindow popupWindow;
//    private ImageView texteditview;
//    private RelativeLayout texteditlayout;
//    private int id=1;
//    private int idPositionX=0;
//    float lastX,lastY;
//    private int containerHeight;
//    private int containerWidth;
//    private  int getById=0;
//    private int getIdFlag=0;
//    private int textCount=0;
//    private float rotateAngle=0;
//    private Animation mAnimation;
//    private Paint paint;
//    private Canvas canvas;
//    private float startX;
//    private float startY;
//    private Bitmap bitcopy;
//    private Bitmap bitmap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        button=findViewById(R.id.test);
//        texteditview=findViewById(R.id.textEditView);
//        texteditlayout=findViewById(R.id.textEditLayout);
//        maxButton=findViewById(R.id.maxButton);
//        minButton=findViewById(R.id.minButton);
//        graffiti=findViewById(R.id.graffiti);
//        removeButton=findViewById(R.id.removeButton);
//        rotateButton=findViewById(R.id.rotateButton);
//
//        graffiti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final View view =getLayoutInflater().inflate(R.layout.popupwindow_inputgraffiti,null);
//                final Button button=view.findViewById(R.id.makeGraffitiButton);
//
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final ImageView imageView=new ImageView((getApplicationContext()));
//
//                        getById=id;
//                        imageView.setId(id++);
//                        imageView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(MainActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        imageView.setX(100);
//                        imageView.setY(100);
//                        //bitcopy = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                        //bitmap=Bitmap.createBitmap(200,200,Bitmap.Config.ARGB_8888);
//                        //bitmap=Bitmap.createBitmap(bitcopy);
//                        Matrix matrix=new Matrix();
//                        matrix.postScale((float) 0.2,(float) 0.2);
//
//                        bitmap=Bitmap.createBitmap(bitcopy,0,0,bitcopy.getWidth(),bitcopy.getHeight(),matrix,true);
//                        imageView.setImageBitmap(bitmap);
//                        //imageView.setScaleX((float) 0.2);
//                       //imageView.setScaleY((float) 0.2);
//                        //imageView.setMaxHeight(1);
//                        //imageView.setMaxWidth(1);
//                        getIdFlag=2;
//                        moveImage(imageView,2);
//
//                        texteditlayout.addView(imageView);
//                    }
//                });
//               // ImageView imageViewXXX=findViewById(getById);
//                //moveImage(imageViewXXX);
//
//
//
//                final ImageView imageView=view.findViewById(R.id.imageGraffiti);
//                getViewHeight(imageView);
//                imageView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        int action = event.getAction();
//                        switch (action) {
//                            // 用户手指摸到屏幕
//                            case MotionEvent.ACTION_DOWN:
////                        if (bitcopy == null) {
////                            bitcopy = Bitmap.createBitmap(hbImageView.getWidth(),
////                                    hbImageView.getHeight(), Bitmap.Config.ARGB_8888);
////                            canvas = new Canvas(bitcopy);
//////                        canvas.drawColor(Color.WHITE);
////
////                        }
//
//
//
//
//
//
//
//                                startX = (int) event.getX();
//                                startY = (int) event.getY();
//                                break;
//                            // 用户手指正在滑动
//                            case MotionEvent.ACTION_MOVE:
//                                float x = event.getX();
//                                float y = event.getY();
//                                canvas.drawLine(startX, startY, x, y, paint);
//                                canvas.save();
//                                canvas.restore();
//                                // 每次绘制完毕之后，本次绘制的结束坐标变成下一次绘制的初始坐标
//                                startX = x;
//                                startY = y;
//                                imageView.setImageBitmap(bitcopy);
//                                break;
//                            // 用户手指离开屏幕
//                            case MotionEvent.ACTION_UP:
//                                break;
//
//                        }
//                        // true：告诉系统，这个触摸事件由我来处理
//                        // false：告诉系统，这个触摸事件我不处理，这时系统会把触摸事件传递给imageview的父节点
//                        return true;
//                    }
//                });
//
//                popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setFocusable(true);
//                popupWindow.setOutsideTouchable(true);
//                popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                popupWindow.setAnimationStyle(R.style.anim_bottom_pop);
//
//                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
//
//
//            }
//        });
//        rotateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                TextView textView=findViewById(getById);
////                textView.setRotation(90);
//                if(rotateAngle>360)
//                    rotateAngle=0;
//                rotateAngle=rotateAngle+90;
//
//                if(getIdFlag==1){
//                    TextView textView=findViewById(getById);
//                    textView.setRotation(rotateAngle);
//                }
//                else if(getIdFlag==2) {
//                    ImageView imageView = findViewById(getById);
//                    imageView.setRotation(rotateAngle);
//                }
//
//
//
////              final ImageView imageView=findViewById(getById);
////              imageView.setRotation(rotateAngle);
//
//
////                mAnimation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_angle);
////                mAnimation.setFillAfter(true);
////                imageView.setAnimation(mAnimation);
////                imageView.refreshDrawableState();
//
//
////                RotateAnimation rotate= (RotateAnimation) AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_angle);
////                //rotate.setFillAfter(true);
////                imageView.setAnimation(rotate);
//
//
//            }
//        });
//        removeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(getById!=0)
//                texteditlayout.removeViewAt(getById);
//                getById=0;
//            }
//        });
//        maxButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                TextView textView=findViewById(getById);
////                float x=textView.getScaleX();
////                float y=textView.getScaleY();
////
////                if(x>10||y>10)
////                {
////                    textView.setScaleX(10);
////                    textView.setScaleY(10);
////                }
////                else
////                {
////                    textView.setScaleX(x+1);
////                    textView.setScaleY(y+1);
////                }
//                if(getIdFlag==1){
//                    TextView textView=findViewById(getById);
//                    maxView(textView);
//                }
//                else if(getIdFlag==2) {
//                    ImageView imageView = findViewById(getById);
//                    maxView(imageView);
//                }
//
////                float x=imageView.getScaleX();
////                float y=imageView.getScaleY();
////                if(x>10||y>10)
////                {
////                    imageView.setScaleX(10);
////                    imageView.setScaleY(10);
////                }
////                else
////                {
////                    imageView.setScaleX((float) (x+0.1));
////                    imageView.setScaleY((float) (y+0.1));
////                }
//
//
//            }
//        });
//
//        minButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(getIdFlag==1){
//                    TextView textView=findViewById(getById);
//                    minView(textView);
//                }
//                else if(getIdFlag==2) {
//                    ImageView imageView = findViewById(getById);
//                    minView(imageView);
//                }
//
//
////                TextView textView=findViewById(getById);
////                float x=textView.getScaleX();
////                float y=textView.getScaleY();
////                if(x<1||y<1)
////                {
////                    textView.setScaleX(1);
////                    textView.setScaleY(1);
////                }
////                else
////                {
////                    textView.setScaleX(x - 1);
////                    textView.setScaleY(y - 1);
////                }
//            }
//        });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final View view =getLayoutInflater().inflate(R.layout.popupwindow_inputtext,null);
//                final EditText editText=view.findViewById(R.id.popupWindowET_1);
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        TextView textView=findViewById(getById);
//                        textView.setText(s.toString());
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//                editText.setFocusable(true);
//                editText.setFocusableInTouchMode(true);
//                editText.requestFocus();
//                InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.showSoftInput(editText, 0);
//
//
//                popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setFocusable(true);
//                popupWindow.setOutsideTouchable(true);
//                popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                popupWindow.setAnimationStyle(R.style.anim_bottom_pop);
//
//                popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
////                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
////                    @Override
////                    public void onDismiss() {
////
////                    }
////                });
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask()
//                {
//                    public void run()
//                    {
//                        InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        inputManager.showSoftInput(editText, 0);
//                    }
//                },200);
//
//                final TextView textView =new TextView(getApplicationContext());
//                getById=id;
//                textView.setId(id++);
//                textView.setText("hahhah");
//                textView.setBackgroundColor(10);
//                getIdFlag=1;
//                moveImage(textView,1);
//////=================================================================
////                textView.setOnTouchListener(new View.OnTouchListener() {
////                    @Override
////                    public boolean onTouch(View v, MotionEvent event) {
////                        switch (event.getActionMasked()) {
////                            case MotionEvent.ACTION_DOWN:
////                                getById=v.getId();
////                                textCount++;
////                                if(textCount==2)
////                                {
////                                    textCount=0;
////                                    //getById=v.getId();
////                                    editText.setFocusable(true);
////                                    editText.setFocusableInTouchMode(true);
////
////                                    editText.setText(textView.getText().toString());
////                                    editText.requestFocus();
////                                    editText.setSelection(editText.getText().length());
////                                    InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
////                                    inputManager.showSoftInput(editText, 0);
////
////
////                                    popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////                                    popupWindow.setFocusable(true);
////                                    popupWindow.setOutsideTouchable(true);
////                                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
////                                    popupWindow.setAnimationStyle(R.style.anim_bottom_pop);
////
////                                    popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
////                                    popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
////                                    Timer timer = new Timer();
////                                    timer.schedule(new TimerTask()
////                                    {
////                                        public void run()
////                                        {
////                                            InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
////                                            inputManager.showSoftInput(editText, 0);
////                                        }
////                                    },200);
////
////
////                                }
////                                lastX = event.getRawX();
////                                lastY = event.getRawY();
////
////                                Toast.makeText(MainActivity.this,v.getScaleX()+"xx",Toast.LENGTH_SHORT).show();
////                                return true;
////                            case MotionEvent.ACTION_MOVE:
////                                //  不要直接用getX和getY,这两个获取的数据已经是经过处理的,容易出现图片抖动的情况
////                                float distanceX = lastX - event.getRawX();
////                                float distanceY = lastY - event.getRawY();
////
////                                float nextY = textView.getY() - distanceY;
////                                float nextX = textView.getX() - distanceX;
////
////                                // 不能移出屏幕
////                                if (nextY < 0) {
////                                    nextY = 0;
////                                } else if (nextY > containerHeight - textView.getHeight()) {
////                                    nextY = containerHeight - textView.getHeight();
////                                }
////                                if (nextX < 0)
////                                    nextX = 0;
////                                else if (nextX > containerWidth - textView.getWidth())
////                                    nextX = containerWidth - textView.getWidth();
////
////                                // 属性动画移动
////                                ObjectAnimator y = ObjectAnimator.ofFloat(textView, "y", textView.getY(), nextY);
////                                ObjectAnimator x = ObjectAnimator.ofFloat(textView, "x", textView.getX(), nextX);
////
////                                AnimatorSet animatorSet = new AnimatorSet();
////                                animatorSet.playTogether(x, y);
////                                animatorSet.setDuration(0);
////                                animatorSet.start();
////
////                                lastX = event.getRawX();
////                                lastY = event.getRawY();
//////                                imageView.setY(nextY);
//////
//////                                imageView.setX(nextX);
////                        }
////
////                        return false;
////                    }
////                });
//////========================================================================================
//
//
//                texteditlayout.addView(textView);
//
//                final ImageView imageView=new ImageView((getApplicationContext()));
//
//                imageView.setId(id++);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//                imageView.setX(idPositionX);
//                imageView.setY(50);
////                ImageView.setX(getInt(getApplicationContext(),TYPE_X,0));
////                ImageView.setY(getInt(getApplicationContext(),TYPE_Y,0));
//                getIdFlag=2;
//                moveImage(imageView,2);
////                imageView.setOnTouchListener(new View.OnTouchListener() {
////                    @Override
////                    public boolean onTouch(View v, MotionEvent event) {
////                        switch (event.getActionMasked()) {
////                            case MotionEvent.ACTION_DOWN:
////                                lastX = event.getRawX();
////                                lastY = event.getRawY();
////                                getById=v.getId();
////                                Toast.makeText(MainActivity.this,v.getScaleX()+"x"+v.getScaleY()+"y",Toast.LENGTH_SHORT).show();
////                                return true;
////                            case MotionEvent.ACTION_MOVE:
////                                //  不要直接用getX和getY,这两个获取的数据已经是经过处理的,容易出现图片抖动的情况
////                                float distanceX = lastX - event.getRawX();
////                                float distanceY = lastY - event.getRawY();
////
////                                float nextY = imageView.getY() - distanceY;
////                                float nextX = imageView.getX() - distanceX;
////
////                                // 不能移出屏幕
////                                if (nextY < 0) {
////                                    nextY = 0;
////                                } else if (nextY > containerHeight - imageView.getHeight()) {
////                                    nextY = containerHeight - imageView.getHeight();
////                                }
////                                if (nextX < 0)
////                                    nextX = 0;
////                                else if (nextX > containerWidth - imageView.getWidth())
////                                    nextX = containerWidth - imageView.getWidth();
////
////                                // 属性动画移动
////                                ObjectAnimator y = ObjectAnimator.ofFloat(imageView, "y", imageView.getY(), nextY);
////                                ObjectAnimator x = ObjectAnimator.ofFloat(imageView, "x", imageView.getX(), nextX);
////
////                                AnimatorSet animatorSet = new AnimatorSet();
////                                animatorSet.playTogether(x, y);
////                                animatorSet.setDuration(0);
////                                animatorSet.start();
////
////                                lastX = event.getRawX();
////                                lastY = event.getRawY();
//////                                imageView.setY(nextY);
//////
//////                                imageView.setX(nextX);
////                        }
////
////                        return false;
////                    }
////                });
//
//
//
//                imageView.setImageResource(R.drawable.xiaoai);
//
//
//                //idPositionX=idPositionX+
//                texteditlayout.addView(imageView);
//
//
//
//
////                ImageView imageView1=new ImageView((getApplicationContext()));
////                imageView1.setId(R.id.textEditView1);
////                imageView1.setImageResource(R.drawable.xiaoai);
////
////                texteditlayout.addView(imageView1);
//
//
//            }
//        });
//
//
//    }
//    public  void moveImage(final View view, final int idFlag){
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getActionMasked()) {
//                    case MotionEvent.ACTION_DOWN:
//                        lastX = event.getRawX();
//                        lastY = event.getRawY();
//                        getById=v.getId();
//                        getIdFlag=idFlag;
//                        Toast.makeText(MainActivity.this,v.getWidth()+"x"+v.getHeight()+"y",Toast.LENGTH_SHORT).show();
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        //  不要直接用getX和getY,这两个获取的数据已经是经过处理的,容易出现图片抖动的情况
//                        float distanceX = lastX - event.getRawX();
//                        float distanceY = lastY - event.getRawY();
//
//                        float nextY = view.getY() - distanceY;
//                        float nextX = view.getX() - distanceX;
//
//                        // 不能移出屏幕
//                        if (nextY < 0) {
//                            nextY = 0;
//                        } else if (nextY > containerHeight - view.getHeight()) {
//                            nextY = containerHeight - view.getHeight();
//                        }
//                        if (nextX < 0)
//                            nextX = 0;
//                        else if (nextX > containerWidth - view.getWidth())
//                            nextX = containerWidth - view.getWidth();
//
//                        // 属性动画移动
//                        ObjectAnimator y = ObjectAnimator.ofFloat(view, "y", view.getY(), nextY);
//                        ObjectAnimator x = ObjectAnimator.ofFloat(view, "x", view.getX(), nextX);
//
//                        AnimatorSet animatorSet = new AnimatorSet();
//                        animatorSet.playTogether(x, y);
//                        animatorSet.setDuration(0);
//                        animatorSet.start();
//
//                        lastX = event.getRawX();
//                        lastY = event.getRawY();
////                                imageView.setY(nextY);
////
////                                imageView.setX(nextX);
//                }
//
//                return false;
//            }
//        });
//    }
//    public void maxView(View view){
//        float x=view.getScaleX();
//        float y=view.getScaleY();
//        if(x>10||y>10)
//        {
//            view.setScaleX(10);
//            view.setScaleY(10);
//        }
//        else
//        {
//            view.setScaleX((float) (x+0.1));
//            view.setScaleY((float) (y+0.1));
//        }
//    }
//    public  void minView(View view)
//    {
//        float x=view.getScaleX();
//        float y=view.getScaleY();
//        if(x<0.2||y<0.2)
//        {
//            view.setScaleX((float) 0.1);
//            view.setScaleY((float) 0.1);
//        }
//        else
//        {
//            view.setScaleX((float) (x - 0.1));
//            view.setScaleY((float) (y - 0.1));
//        }
//    }
//    public void getViewHeight(final View view) {
//        int height = view.getHeight();
//
//        Log.d("Debug", "oncreat获取高度：" + height);//获取的高度为0 ，所以不能直接去获取高度
//        view.post(new Runnable() {
//            @Override
//            public void run() {
//                int height = view.getHeight();
//                int width=view.getWidth();
//                //ViewHight=view.getHeight();
//                bitcopy = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                //bitcopy= Bitmap.createBitmap(width,height,bitsrc.getConfig());
//                //bitcopy=Bitmap.createBitmap(bitsrc);
//                //bitcopy=bitsrc;
//                paint = new Paint();
//                // 3.创建画板对象，把白纸铺在画板上
//                paint.setStrokeWidth(15);
//                canvas = new Canvas(bitcopy);
//                //paint.setTextSize(50);
//                canvas.drawBitmap(bitcopy,new Matrix(),paint);
//               //Log.d("Debug", "post中获取高度：" + ViewHight);
//
//            }
//        });
//
//        //return  ViewHight;
//    }
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        // 这里来获取容器的宽和高
//        if (hasFocus) {
//            containerHeight = texteditlayout.getHeight();
//            containerWidth = texteditlayout.getWidth();
//        }
//    }
//
////    public void setContentView(int contentView) {
////        this.contentView = contentView;
////    }
////    @Override
////    public void onClick(View v) {
////        // 判断当前点击了哪一个
////        for (int i = 0; i < ll_titles.getChildCount(); i++) {
////            TextView tv = (TextView) ll_titles.getChildAt(i);
////            if (v == tv) {
////                // 让对应的这个textView变红
////                tv.setTextColor(Color.RED);
////                viewPager.setCurrentItem(i);
////            } else {
////                tv.setTextColor(Color.GRAY);
////            }
////        }
////    }
//
//}
