<template>
    <div class="setting-import">
        <div class="set-title">
            <zs-icon icon="draft" :size="18"></zs-icon>
            请选择文件导入<span class="download" @click="handleServiceItemTemplate">模板下载</span></div>
        <div class="set-body" v-if="step==1">
            <file-upload button="primary" class="file-upload" @get-files="handleGetFiles" text="选择文件"></file-upload>
        </div>
        <div v-if="step==2" class="set-body2">
            <div class="file">
                <zs-icon class="success" icon="circle-success" size="18"></zs-icon>
                <zs-icon class="biao" icon="biao" size="30"></zs-icon>
                <p class="file-name">{{currFile.name}}</p>
                <file-upload button="primary" class="re-select-file" @get-files="handleGetFiles"
                             text="重新选择"></file-upload>
            </div>
            <zs-button class="up" type="primary" @click="insertUpload">
                <zs-icon icon="upload" size="20"></zs-icon>
                立即导入
            </zs-button>
        </div>
        <div v-if="step==3" class="set-body3">
            <p class="import">
                <zs-icon class="biao" icon="circle-success" size="25"></zs-icon>
                导入结束
            </p>
            <p class="success-error">{{`${successNum}条数据导入成功，${failNum}条数据导入失败`}}</p>
            <p class="download-result" @click="handleDownloadResult">
                <zs-icon icon="down" size="20px"></zs-icon>
                下载导入结果
            </p>
            <file-upload button="primary" class="file-upload" @get-files="handleGetFiles" text="继续导入"></file-upload>
        </div>
    </div>
</template>

<script>
    import { turnToNextPage } from '../../../../../../../assets/js/utils';
    import fileUpload from '../../../../../../../components_proj/file_upload/file_upload.vue';

    export default {
        name: 'member',
        components: {
            fileUpload
        },
        data() {
            return {
                step: 1,
                currFile: {},
                successNum: 0,
                failNum: 0
            };
        },
        methods: {
            /* ------------------- insert (增) start -------------------*/
            /**
             *  上传文件到后台
             */
            insertUpload() {
                const formData = new FormData();

                formData.append('file', this.currFile);
                this.$ajax(
                    this.$joggle.merchant.setting.importUserInfo,
                    formData,
                    true,
                    (data, loading) => {
                        loading.close();
                        if (data.code === 'ZS011000') {
                            this.successNum = data.data.successNum;
                            this.failNum = data.data.failNum;
                            this.step = 3;
                        } else {
                            this.$message({
                                type: 'success',
                                message: data.msg
                            });
                        }
                    });
            },
            /* ------------------- insert (增) end -------------------*/

            /* ------------------- delete (删) start -------------------*/

            /* ------------------- delete (删) end -------------------*/

            /* ------------------- update (改) start -------------------*/

            /* ------------------- update (改) end -------------------*/

            /* ------------------- select (查) start -------------------*/

            /* ------------------- select (查) end -------------------*/

            /* ------------------- handle (操) start -------------------*/
            /**
             *  下载基础模板
             */
            handleServiceItemTemplate() {
                turnToNextPage(this.$joggle.merchant.setting.userInfoTemplate, {}, true);
            },
            /**
             *  上传选择文件到当前页面
             * @param file 对应表格格式的文件
             */
            handleGetFiles(file) {
                const fileType = file.name.split('.')[1];

                if (['xlsx', 'xls'].indexOf(fileType) === -1) {
                    this.$message({
                        type: 'error',
                        message: '文件格式有误！'
                    });
                } else {
                    this.currFile = file;
                    this.step = 2;
                }
            },
            /**
             *  下载导入结果
             */
            handleDownloadResult() {
                turnToNextPage(this.$joggle.merchant.setting.userInfoResult, {}, true);
            }
            /* ------------------- handle (操) end -------------------*/


        }
    };
</script>

<style lang="less">
    @import './style.less';
</style>