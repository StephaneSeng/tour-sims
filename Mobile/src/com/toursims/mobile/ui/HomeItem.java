package com.toursims.mobile.ui;

import android.view.View.OnClickListener;

public class HomeItem {

        private OnClickListener function;
        private int text;
        private int pictureURL;
        private Class<?> c;
        
        public HomeItem() {
                // TODO Auto-generated constructor stub
        }

        public OnClickListener getFunction() {
                return function;
        }

        public void setFunction(OnClickListener function) {
                this.function = function;
        }

        public int getText() {
                return text;
        }

        public void setText(int text) {
                this.text = text;
        }

        public int getPictureURL() {
                return pictureURL;
        }

        public void setPictureURL(int pictureURL) {
                this.pictureURL = pictureURL;
        }

        public HomeItem(int text, int pictureURL,OnClickListener function) {
                super();
                this.function = function;
                this.text = text;
                this.pictureURL = pictureURL;
        }
        
        public HomeItem(int text, int pictureURL){
        	this.text = text;
        	this.pictureURL = pictureURL;
        }
        
        public HomeItem(int text, int pictureURL,Class<?> c) {
                super();
                this.c = c;
                this.text = text;
                this.pictureURL = pictureURL;
        }       
        
        public Class<?> getC() {
			return c;
		}
        
        public void setC(Class<?> c) {
			this.c = c;
		}
}