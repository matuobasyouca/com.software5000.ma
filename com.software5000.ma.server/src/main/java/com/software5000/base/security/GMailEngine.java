package com.software5000.base.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.ImageFilter;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.WaterFilter;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.ComposeDictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

/**
 * JCaptcha验证码图片生成引擎,仿照JCaptcha2.0编写类似GMail验证码的样式.
 * 
 * @author liukai
 * 
 */
public class GMailEngine extends ListImageCaptchaEngine {
	@Override
	protected void buildInitialFactories() {
		int minWordLength = 4;
		int maxWordLength = 4;
//		int fontSize = 50;
//		int imageWidth = 250;
//		int imageHeight = 100
		int fontSize = 30;
		int imageWidth = 120;
		int imageHeight = 50;;

		//word generator
		WordGenerator dictionnaryWords = new ComposeDictionaryWordGenerator(new FileDictionary("toddlist"));

		//word2image components
		TextPaster randomPaster = new DecoratedRandomTextPaster(minWordLength, maxWordLength,
				new RandomListColorGenerator(new Color[] { new Color(23, 170, 27), new Color(220, 34, 11),
						new Color(23, 67, 172) }), new TextDecorator[] {});
		BackgroundGenerator background = new UniColorBackgroundGenerator(imageWidth, imageHeight, new Color(121,201,254));
		FontGenerator font = new RandomFontGenerator(fontSize, fontSize, new Font[] {
				new Font("nyala", Font.BOLD, fontSize), new Font("Bell MT", Font.PLAIN, fontSize),
				new Font("Credit valley", Font.BOLD, fontSize) });

		ImageDeformation postDef = new ImageDeformationByFilters(new ImageFilter[] {});
		ImageDeformation backDef = new ImageDeformationByFilters(new ImageFilter[] {});
		ImageDeformation textDef = new ImageDeformationByFilters(new ImageFilter[] {});

		WordToImage word2image = new DeformedComposedWordToImage(font, background, randomPaster, backDef, textDef,
				postDef);
		addFactory(new GimpyFactory(dictionnaryWords, word2image));
	}

//	@Override
	protected void buildInitialFactories0() {
		int minWordLength = 4;
		int maxWordLength = 4;
		int fontSize = 35;
		int imageWidth = 120;
		int imageHeight = 50;

		// word generator
		WordGenerator dictionnaryWords = new ComposeDictionaryWordGenerator(
				new FileDictionary("toddlist"));
		// WordGenerator dictionnaryWords = new
		// RandomWordGenerator("23456789ABCDEFGHabcdefghijk");
		// word2image components
		TextPaster randomPaster = new DecoratedRandomTextPaster(minWordLength,
				maxWordLength, new RandomListColorGenerator(new Color[] {
						new Color(23, 170, 27), new Color(220, 34, 11),
						new Color(23, 67, 172) }), new TextDecorator[] {});

		RandomRangeColorGenerator color = new RandomRangeColorGenerator(
		/* 74 */new int[] { 255, 255 },
		/* 75 */new int[] { 255, 255 },
		/* 76 */new int[] { 255, 255 });

		BackgroundGenerator background = new UniColorBackgroundGenerator(
				imageWidth, imageHeight, color);
		FontGenerator font = new RandomFontGenerator(fontSize, fontSize,
				new Font[] { new Font("nyala", Font.BOLD, fontSize),
						new Font("Bell MT", Font.PLAIN, fontSize),
						new Font("Credit valley", Font.BOLD, fontSize) });

		// 过滤器
		//
		// WeaveFilter weave = new WeaveFilter();
		// weave.setYWidth(20d);
		// weave.setYGap(5d);

		RippleFilter ripplefilter = new RippleFilter();
		ripplefilter.setWaveType(2);
		ripplefilter.setXWavelength(12d);

		// SparkleFilter doGFilter = new SparkleFilter();
		// doGFilter.setAmount(10000);
		WaterFilter water = new WaterFilter();
		water.setAmplitude(2d);// 振幅
		water.setAntialias(true);// 显示字会出现锯齿状,true就是平滑的
		water.setPhase(30d);// 月亮的盈亏
		water.setWavelength(60d);//

		RippleFilter rippleFilter = new RippleFilter();
		rippleFilter.setWaveType(RippleFilter.SINE);
		rippleFilter.setXAmplitude(2.6f);
		rippleFilter.setYAmplitude(1.7f);
		rippleFilter.setXWavelength(15);
		rippleFilter.setYWavelength(5);
		// rippleFilter.setEdgeAction(TransformFilter.NEAREST_NEIGHBOUR);

		WaterFilter waterFilter = new WaterFilter();
		waterFilter.setAmplitude(1.5f);
		waterFilter.setPhase(10);
		waterFilter.setWavelength(2);

		ImageDeformation postDef = new ImageDeformationByFilters(
				new ImageFilter[] { water });
		ImageDeformation backDef = new ImageDeformationByFilters(
				new ImageFilter[] {});
		ImageDeformation textDef = new ImageDeformationByFilters(
				new ImageFilter[] {});

		WordToImage word2image = new DeformedComposedWordToImage(font,
				background, randomPaster, backDef, textDef, postDef);
		addFactory(new GimpyFactory(dictionnaryWords, word2image));
	}

	// @Override
	protected void buildInitialFactories2() {
		int minWordLength = 4;
		int maxWordLength = 5;
		int fontSize = 50;
		int imageWidth = 250;
		int imageHeight = 100;
		WordGenerator dictionnaryWords = new ComposeDictionaryWordGenerator(
				new FileDictionary("toddlist"));

		// word2image components
		TextPaster randomPaster = new DecoratedRandomTextPaster(minWordLength,
				maxWordLength, new RandomListColorGenerator(new Color[] {
						new Color(23, 170, 27), new Color(220, 34, 11),
						new Color(23, 67, 172) }), new TextDecorator[] {});
		BackgroundGenerator background = new UniColorBackgroundGenerator(
				imageWidth, imageHeight, Color.white);
		FontGenerator font = new RandomFontGenerator(fontSize / 7, fontSize,
				new Font[] { new Font("nyala", Font.BOLD, fontSize),
						new Font("Bell MT", Font.PLAIN, fontSize),
						new Font("Credit valley", Font.BOLD, fontSize) });
		ImageDeformation postDef = new ImageDeformationByFilters(
				new ImageFilter[] {});
		ImageDeformation backDef = new ImageDeformationByFilters(
				new ImageFilter[] {});
		ImageDeformation textDef = new ImageDeformationByFilters(
				new ImageFilter[] {});

		WordToImage word2image = new DeformedComposedWordToImage(font,
				background, randomPaster, backDef, textDef, postDef);
		addFactory(new GimpyFactory(dictionnaryWords, word2image));
	}

}
