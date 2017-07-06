# Android Image Test

### A test of Android image, where you can decide the RGB value of each pixel in a bitmap depends on the position of the pixel.

## How to use?

1、 First, initialize ColorHolder with your specified width and height, or call the constructor with no arguments which would initialize width and height by 1024:

```java
ColorHolder holder = new ColorHolder();
//or
ColorHolder holder = new ColorHolder(1080, 1920);
```

2、Implements ColorHolder.ColorStrategy or use provided strategy to generate color array, and put your next operates in the Callback

```java
holder.setStrategy(new Mandelbrot1())
        .setCallback(new ColorHolder.Callback() {
            @Override
            public void onColorsCreated() {
                // create a bitmap using the color array generated just now
                Bitmap bitmap = holder.createBitmap();
                // show the bitmap
                imageView.setImageBitmap(bitmap);
                // write the bitmap to storage
                FileUtils.writeBitmapToStorage(bitmap);
            }
        })
        .startInParallel();
```

*When implements ColorHolder.ColorStrategy, you should implements the three methods below:*

```java
    int getRed(int i, int j);

    int getGreen(int i, int j);

    int getBlue(int i, int j);
```

**This test is simple, aiming to throw away a brick in order to get a gem. You can generate numerous different pictures theoretically, so there are lots of other things you can do when you extend this example.**
