package com.example.chronos_hm40;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

public class ColorRectangleView extends View {
    private int color;
    private int width;
    private int height;
    private String title;
    private String subtitle;
    private String hour_begin;
    private String hour_end;

    public ColorRectangleView(Context context, int color, int width, int height, String title, String subtitle, String hour_begin, String hour_end) {
        super(context);
        this.color = color;
        this.width = width;
        this.height = height;
        this.title = title;
        this.subtitle = subtitle;
        this.hour_begin = hour_begin;
        this.hour_end = hour_end;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate(); // Rafraîchit la vue pour afficher la nouvelle couleur
    }

    public int getColor() {
        return color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ShapeDrawable shadowDrawable = new ShapeDrawable(new RectShape());
        shadowDrawable.getPaint().setColor(Color.GRAY);
        shadowDrawable.getPaint().setShadowLayer(10, 0, 0, Color.BLACK);
        shadowDrawable.setBounds(0, 0, width, height);
        shadowDrawable.draw(canvas);

        // Dessiner le rectangle
        Paint rectanglePaint = new Paint();
        rectanglePaint.setColor(color);
        canvas.drawRect(0, 0, width, height, rectanglePaint);

        // Dessiner le titre en gras au centre
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(0.2f * height); // Augmenter la taille de police
        titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        float titleX = width / 2f;
        float titleY = (height - titlePaint.descent() - titlePaint.ascent()) / 2f - (0.15f * height); // Remonter légèrement le titre
        canvas.drawText(title, titleX, titleY, titlePaint);

        // Dessiner le sous-titre normal
        Paint subtitlePaint = new Paint();
        subtitlePaint.setColor(Color.BLACK);
        subtitlePaint.setTextSize(0.12f * height); // Augmenter la taille de police
        subtitlePaint.setTextAlign(Paint.Align.CENTER);
        float subtitleX = width / 2f;
        float subtitleY = (height - subtitlePaint.descent() - subtitlePaint.ascent()) / 2f + (0.1f * height); // Remonter légèrement le sous-titre
        canvas.drawText(subtitle, subtitleX, subtitleY, subtitlePaint);

        // Dessiner l'horaire de départ en bas à gauche
        Paint startTimePaint = new Paint();
        startTimePaint.setColor(Color.BLACK);
        startTimePaint.setTextSize(0.15f * height); // Augmenter la taille de police
        startTimePaint.setTextAlign(Paint.Align.LEFT);
        float startTimeX = 0.05f * width; // Décalage vers la droite pour éviter les bords
        float startTimeY = height - startTimePaint.descent();
        canvas.drawText(hour_begin, startTimeX, startTimeY, startTimePaint);

        // Dessiner l'horaire de fin en bas à droite
        Paint endTimePaint = new Paint();
        endTimePaint.setColor(Color.BLACK);
        endTimePaint.setTextSize(0.15f * height); // Augmenter la taille de police
        endTimePaint.setTextAlign(Paint.Align.RIGHT);
        float endTimeX = width - (0.05f * width); // Décalage vers la gauche pour éviter les bords
        float endTimeY = height - endTimePaint.descent();
        canvas.drawText(hour_end, endTimeX, endTimeY, endTimePaint);
    }
}
