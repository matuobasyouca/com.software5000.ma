<template>
    <div class="ue">
        <editor :config="editorConfig" @change="handleEditor"></editor>
    </div>
</template>


<script>
    import editor from './ue/app.vue';
    export default {
        components: { editor },
        data() {
            return {
                editorConfig: [
                    'bold',  // 粗体
                    'italic',  // 斜体
                    'underline',  // 下划线
                    'strikeThrough',  // 删除线
                    'foreColor',  // 文字颜色
                    'backColor',  // 背景颜色
                    'list',  // 列表
                    'justify',  // 对齐方式
                    'image',  // 插入图片
                    'undo',  // 撤销
                    'redo'  // 重复
                ]
            };
        },
        methods: {
            handleEditor(html){
                console.log(html);
            }
        }
    };
</script>

<style>
    body {
        padding: 50px !important;
    }

    .ue {
        width: 400px;
    }
</style>