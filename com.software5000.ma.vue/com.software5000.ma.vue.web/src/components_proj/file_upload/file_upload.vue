<template>
    <div class="file-upload-wrap">
        <label for="fileUpload" :style="labelStyle">
            <slot>{{ text }}</slot>
        </label>
        <input id="fileUpload" type="file" @change="handleGetFile" :multiple="multiple">
    </div>
</template>

<script>
    const colorMap = {
        danger: '#fd543d',
        primary: '#4b8dde',
        success: '#1bc91a'
    }
    export default {
        name: 'fileUpload',
        props: {
            text: {
                type: String,
                default: '上传素材'
            },
            button: {
                type: String,
                default: 'primary'
            },
            multiple: Boolean
        },
        computed: {
            labelStyle(){
                let style = {};
                style.backgroundColor = colorMap[this.button];
                return style;
            }
        },
        methods: {
            handleGetFile(e){
                let file = e.target.files;
                let fileArr
                if(this.multiple){
                    fileArr = [];
                    for (let key in file) {
                        fileArr.push(file[key])
                    }
                }else{
                    fileArr = file[0];
                }
                this.$emit('get-files', fileArr);
            }
        }
    };
</script>

<style lang="less">
    @import './file_upload.less';
</style>