package com.ucuxin.ucuxin.tec.model;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author parsonswang
 * 
 */
public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
}
