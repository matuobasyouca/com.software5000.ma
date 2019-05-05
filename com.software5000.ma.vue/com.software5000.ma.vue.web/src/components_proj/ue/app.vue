<template>
    <div class="ue">
        <div id="editorElem" style="text-align:left"></div>
    </div>
</template>

<script>
    import E from './assets/wangEditor';
    export default {
        name: 'editor',
        props: {
            config: {
                type: Array,
                default(){
                    return [];
                }
            },
            defaultContent: ''
        },
        data () {
            return {
                editorContent: '',
                editor: null
            };
        },
        watch: {
            defaultContent(val){
                this.editor.txt.html(val);
            }
        },
        mounted() {
            this.editor = new E('#editorElem');

            this.editor.customConfig.onchange = (html) => {
                this.editorContent = `${html}`;
//                const textHtml = html.split(/\<img.*?\"\>/g);
//                const imgsHtml = html.match(/\<img.*?\"\>/g) || [];
//                let tempHtml = '';
//                let firstP = '';
//                console.log(html);
//                console.log(textHtml);
//                console.log(imgsHtml);
//                //拆分成数组后第一个元素只有<p>或者 <p>带属性
//                if (/^\<p.*?(\"\>)$/.test(textHtml[0]) || textHtml[0] === '<p>') {
//                    firstP = textHtml[0];
//                    textHtml[0] = '';
//                } else if (textHtml[0].indexOf('</p>') > -1) {
//                    //最后一个元素包含</p>
//                    firstP = textHtml[0].match(/^\<p.*?\>/)[0];
//
//                    textHtml[0] = textHtml[0].replace(firstP, '');
//                    textHtml[0] = textHtml[0].replace('</p>', '');
//                } else {
//                    firstP = textHtml[0].match(/^\<p.*?\>/)[0];
//                    textHtml[0] = textHtml[0].replace(firstP, '');
//                }
//
//                if (textHtml.length > 1) {
//                    if (textHtml[textHtml.length - 1] === '<br></p>' || textHtml[textHtml.length - 1] === '</p>') {
//                        textHtml[textHtml.length - 1] = '';
//                    } else if (textHtml[textHtml.length - 1].indexOf('<br></p>') > -1) {
//                        textHtml[textHtml.length - 1] = textHtml[textHtml.length - 1].slice(0, -8);
//                    } else if (textHtml[textHtml.length - 1].indexOf('</p>') > -1) {
//                        textHtml[textHtml.length - 1] = textHtml[textHtml.length - 1].slice(0, -4);
//                    }
//                }
//                textHtml.forEach((h, i) => {
//                    tempHtml += `${h ? h.indexOf('<a>') > -1 || h.indexOf('<li>') > -1 ? h : '<a>' + h + '</a>' : ''}${imgsHtml[i] ? imgsHtml[i] : ''}`;
//                });
                this.$emit('change', this.editorContent);
            };

            //其他配置
            this.config.length > 0 && (this.editor.customConfig.menus = this.config);
            this.editor.customConfig.showLinkImg = false;
            //上传图片事件监听
            const _this = this;
            this.editor.customConfig.customUploadImg = function (files, insert) {
                const formData = new FormData();

                formData.append('image', files[0]);
                formData.append('type', 'packClusterImg');
                _this.$ajax(
                    _this.$joggle.mzscp.uploadImgForFile,
                    formData,
                    false,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            insert(data.data.imgSrc);
                        }
                    }
                );
            };
            //配置编辑区域的 z-index
            this.editor.customConfig.zIndex = 100;

            this.editor.create();
            this.editor.txt.html(this.defaultContent);
        }
    };
</script>
<style lang="less">
    @import './style.less';
</style>