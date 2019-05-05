/**
 * Created by JANZ on 2017/3/23.
 */
const turnToBase = function (obj, tarSize, fn) {

    let self = obj;
    let targetSize = tarSize;
    let file = self.files[0];
    let r = new FileReader();

    r.readAsDataURL(file);

    r.onload = function () {

        let fileStream = this.result;
        let URL = window.URL || window.webkitURL;
        let canvas = document.createElement('canvas');
        let ctx = canvas.getContext('2d');

        if (URL && File && ctx) {
            // var fileURL = URL.createObjectURL(file),
            let img = new Image();

            img.src = fileStream;
            img.onload = function () {

                let drawWidth = img.width;
                let drawHeight = img.height;
                let maxSize = Math.max(drawWidth, drawHeight);
                let tarSize = targetSize;

                if (maxSize > tarSize) {
                    let minSize = Math.min(drawWidth, drawHeight);

                    minSize = minSize / maxSize * tarSize;
                    maxSize = tarSize;
                    if (drawWidth > drawHeight) {
                        drawWidth = maxSize;
                        drawHeight = minSize;
                    } else {
                        drawWidth = minSize;
                        drawHeight = maxSize;
                    }
                }
                canvas.width = drawWidth;
                canvas.height = drawHeight;

                ctx.drawImage(img, 0, 0, drawWidth, drawHeight);
                let base64 = canvas.toDataURL('image/jpeg');

                if (fn) {
                    fn(base64);
                }
                canvas = null;
                img = null;
            };
        } else if (fn) {
            fn(fileStream);
        }
    };
};

export default turnToBase;
