<template>
    <div style="padding-top: 50px;">
        <ma-head :currId="01"></ma-head>
        <zs-breadcrumb>
            <zs-breadcrumb-item to="http://www.baidu.com">开单1</zs-breadcrumb-item>
            <zs-breadcrumb-item>开单2</zs-breadcrumb-item>
        </zs-breadcrumb>
        <div class="wrapper">
            <div class="block">
                <zs-table
                        :data="datas"
                        :columns="columns"
                        :context="context"
                        :no-data-colspan="10"
                        no-data-text="暂无客户"
                        no-data-icon="data-empty-member"
                        border>
                </zs-table>
            </div>
            <tab :tabs="['测试1','测试2','测试3']" :current="currIndex" @change="handleTabChange"></tab>
        </div>
        <div class="block">
            <zs-icon icon="user" text="user"></zs-icon>
            <br>
            <zs-icon icon="user1" text="user1"></zs-icon>
            <br>
            <zs-icon icon="user2" text="user2"></zs-icon>
            <br>
            <zs-icon icon="search1" text="search1"></zs-icon>
            <br>
            <zs-icon icon="search2" text="search2"></zs-icon>
            <br>
            <zs-icon icon="arrow-left" text="arrow-left"></zs-icon>
            <br>
            <zs-icon icon="arrow-right" text="arrow-right"></zs-icon>
            <br>
            <zs-icon icon="arrow-top" text="arrow-top"></zs-icon>
            <br>
            <zs-icon icon="arrow-bottom" text="arrow-bottom"></zs-icon>
            <br>
            <zs-icon icon="dot" text="dot"></zs-icon>
            <br>
            <zs-icon icon="dot-large" text="dot-large"></zs-icon>
            <br>
            <zs-icon icon="dot-gray" text="dot-gray"></zs-icon>
            <br>
            <zs-icon icon="dot-large-gray" text="dot-large-gray"></zs-icon>
            <br>
            <zs-icon icon="caret-top" text="caret-top"></zs-icon>
            <br>
            <zs-icon icon="caret-bottom" text="caret-bottom"></zs-icon>
            <br>
            <zs-icon icon="ten-cross" text="ten-cross"></zs-icon>
            <br>
            <zs-icon icon="cross" text="cross"></zs-icon>
            <br>
            <zs-icon icon="add" text="add"></zs-icon>
            <br>
            <zs-icon icon="circle-cross" text="circle-cross"></zs-icon>
            <br>
            <zs-icon icon="circle-error" text="circle-error"></zs-icon>
            <br>
            <zs-icon icon="tick" text="tick"></zs-icon>
            <br>
            <zs-icon icon="circle-success" text="circle-success"></zs-icon>
            <br>
            <zs-icon icon="warning" text="warning"></zs-icon>
            <br>
            <zs-icon icon="danger" text="danger"></zs-icon>
            <br>
            <zs-icon icon="pwd" text="pwd"></zs-icon>
            <br>
            <zs-icon icon="quit" text="quit"></zs-icon>
            <br>
            <zs-icon icon="logo-small" text="logo_small"></zs-icon>
            <br>
            <zs-icon icon="tri-dot" text="tri-dot"></zs-icon>
            <br>
            <zs-icon icon="data-empty" text="data-empty"></zs-icon>
            <br>
            <zs-icon icon="data-empty-member" text="data-empty-member"></zs-icon>
            <br>
            <zs-icon icon="delete" text="delete"></zs-icon>
            <br>
            <zs-icon icon="delete2" text="delete2"></zs-icon>
            <br>
            <zs-icon icon="warning" text="warning"></zs-icon>
            <br>
            <zs-icon icon="upload" text="upload"></zs-icon>
            <br>
            <zs-icon icon="upload" text="download"></zs-icon>
            <br>
            <zs-icon icon="date" text="date"></zs-icon>
            <br>
            <zs-icon icon="wechat" text="wechat"></zs-icon>
            <br>
            <zs-icon icon="cash" text="cash"></zs-icon>
            <br>
            <zs-icon icon="phone" text="phone"></zs-icon>
            <br>
            <zs-icon icon="location" text="location"></zs-icon>
            <br>
            <zs-icon icon="history" text="history"></zs-icon>
            <br>
            <zs-icon icon="draft" text="draft"></zs-icon>
            <br>
            <zs-icon icon="biao" text="biao"></zs-icon>
            <br>
            <zs-icon icon="member" text="member"></zs-icon>
            <br>
            <zs-icon icon="package-card" text="package-card"></zs-icon>
            <br>
            <zs-icon icon="selected" text="selected"></zs-icon>
            <br>
            <zs-icon icon="card" text="card"></zs-icon>
            <br>

            <zs-icon icon="down" text="down"></zs-icon>
            <br>

            <zs-icon icon="draft2" text="draft2"></zs-icon>
            <br>
            <zs-icon icon="car" text="car"></zs-icon>
            <br>
            <zs-icon icon="wechat2" text="wechat2"></zs-icon>
            <br>
            <zs-icon icon="money" text="money"></zs-icon>
            <br>
            <zs-icon icon="dated" text="dated"></zs-icon>
            <br>
            <zs-icon icon="used" text="used"></zs-icon>
            <br>
            <zs-icon icon="draft3" text="draft3"></zs-icon>
            <br>
            <zs-icon icon="merchant" text="merchant"></zs-icon>
            <br>
            <zs-icon icon="time" text="time"></zs-icon>
            <br>
            <zs-icon icon="write" text="write"></zs-icon>
            <br>
            <zs-icon icon="money" text="money"></zs-icon>
            <br>
        </div>
        <div>
            <div style="height: 156px; width: 156px;">
                <svg viewBox="0 0 100 100">
                    <path
                            d="M 50 50 m 0 -47 a 47 47 0 1 1 0 94 a 47 47 0 1 1 0 -94"
                            stroke="#e5e9f2"
                            stroke-width="4.8"
                            fill="none"></path>
                    <path
                            d="M 50 50 m 0 -47 a 47 47 0 1 1 0 94 a 47 47 0 1 1 0 -94"
                            stroke-linecap="round"
                            stroke="#20a0ff"
                            stroke-width="4.8"
                            fill="none"
                            style="stroke-dasharray: 299.08px, 299.08px; stroke-dashoffset: 199.08px; transition: stroke-dashoffset 0.6s ease 0s, stroke 0.6s ease;"></path>
                </svg>
            </div>
        </div>
        <div>
            <p class="title">fileUpload 文件上传组件：</p><br>
            <file-upload
                    @get-files="handleGetFiles"
            ></file-upload>
            <br>
        </div>
    </div>
</template>

<script>
    import maHead from '../../components_proj/ma-head/app.vue';
    import tab from '../../components_proj/tab/app.vue';
    import fileUpload from '../../components_proj/file_upload/file_upload.vue';
    import {isPC, isPhone} from '../../assets/js/utils';
    export default {
        name: 'app',
        components: {
            tab,
            maHead,
            fileUpload,
        },
        data(){
            return {
                datas: [
                    {
                        id: 1,
                        sex: '男',
                        age: 18,
                        like: '打球'
                    },
                    {
                        id: 2,
                        sex: '女',
                        age: 16,
                        like: '唱歌'
                    }
                ],
                columns: [
                    {
                        title: 'selectAll',
                        type: 'selection',
                        width: 60
                    },
                    {
                        title: '性别',
                        key: 'sex',
                        align: 'center',
                        width: 60
                    },
                    {
                        title: '其他',
                        key: 'age',
                        width: 60
                    }
                ],
                context: this,
                currIndex: 1,
                uploadFile: {
                    id: '',
                    materialName: '1yue',
                    materialMajorKey: 'fxla',
                    fileData: {},
                }
            }
        },
        methods: {
            handleTabChange(i){
                this.currIndex = i;
            },
            handleGetFiles(files){
                console.log(files);
                this.uploadFile.fileData = files[0];
            }
        },
        created(){
//            console.log('isPc',isPC())
//            console.log('isPhone',isPhone())
//            const str = 'Hello World!';
//            console.log(str.includes('ll'));
//            console.log(str.includes('lt'));
//            console.log(str.startsWith('Hello'))
//            console.log(str.startsWith('hello'))
//            console.log(str.endsWith('World!'))
//            console.log(str.endsWith('world!'))
//            console.log(str.repeat(3));
//            const x = 1;
//            const y = 2;
//            const z = 3;
//
//            function tag(literals, ...substitutions) {
//                console.log(literals)
//                let result = '';
//                for (let i = 0; i < literals.length; i++) {
//                    result += literals[i];
//                }
//                return result;
//            }
//
//            console.log(tag `a${x}b${y}c${z}d`);
//            const str = `abc\nefg`
//            console.log(str);
//            console.log(String.raw`abc\nefg`);

        }
    }
</script>

<style lang="less">
    @import './style.less';
</style>
