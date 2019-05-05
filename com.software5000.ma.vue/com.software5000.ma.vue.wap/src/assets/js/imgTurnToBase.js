/**
 * Created by JANZ on 2017/3/23.
 */
import EXIF from './exif';
const turnToBase = function (obj, tarSize, fn) {
    let self = obj;
    let targetSize = tarSize;
    let file = self.files[0];
    let r = new FileReader();

    r.readAsDataURL(file);

    r.onload = function () {

        let fileStream = this.result;

        let URL = window.URL || window.webkitURL,
            canvas = document.createElement('canvas'),
            ctx = canvas.getContext('2d');

        if (URL && File && ctx) {
            // let fileURL = URL.createObjectURL(file),
            let img = new Image();

            img.src = fileStream;
            img.onload = function () {
                let orientation;

                EXIF.getData(img, function () {

                    orientation = EXIF.getTag(this, 'Orientation');
                    let degree = 0, drawWidth = img.width, drawHeight = img.height, width, height;
                    //以下改变一下图片大小

                    let maxSide = Math.max(drawWidth, drawHeight);
                    let tarSize = targetSize;

                    if (maxSide > tarSize) {
                        let minSide = Math.min(drawWidth, drawHeight);

                        minSide = minSide / maxSide * tarSize;
                        maxSide = tarSize;
                        if (drawWidth > drawHeight) {
                            drawWidth = maxSide;
                            drawHeight = minSide;
                        } else {
                            drawWidth = minSide;
                            drawHeight = maxSide;
                        }
                    }
                    canvas.width = width = drawWidth;
                    canvas.height = height = drawHeight;
                    switch (orientation) {//横屏竖屏转化
                        //横屏拍摄，此时home键在左侧
                        case 3:
                            degree = 180;
                            drawWidth = -width;
                            drawHeight = -height;
                            break;
                        //竖屏拍摄，此时home键在下方(正常拿手机的方向)
                        case 6:
                            canvas.width = height;
                            canvas.height = width;
                            degree = 90;
                            drawWidth = width;
                            drawHeight = -height;
                            break;
                        //竖屏拍摄，此时home键在上方
                        case 8:
                            canvas.width = height;
                            canvas.height = width;
                            degree = 270;
                            drawWidth = -width;
                            drawHeight = height;
                            break;
                        default:
                    }
                    //使用canvas旋转校正
                    ctx.rotate(degree * Math.PI / 180);
                    ctx.drawImage(img, 0, 0, drawWidth, drawHeight);
                    let base64 = canvas.toDataURL('image/jpeg');

                    if (fn) {
                        fn(base64);
                    }
                    canvas = null;
                    img = null;
                });

            };
        } else if (fn) {
            fn(fileStream);
        }
    };
};

export default turnToBase;