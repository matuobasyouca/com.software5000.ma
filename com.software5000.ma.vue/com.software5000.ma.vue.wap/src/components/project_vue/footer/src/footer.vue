<template>
    <div class="bottom-nav__wrap clr">
        <div class="bottom-nav">
            <a href="index.html" :class="{curr:isIndex}">
                <i class="icon bottom-nav__icon bottom-nav__icon-home"></i>
                <p>首页</p>
            </a>
            <a href="work_order_manage.html" :class="{curr:isOrder}">
                <i class="icon bottom-nav__icon bottom-nav__icon-order"></i>
                <p>工单</p>
            </a>
            <a href="member_management.html" :class="{curr:isMember}">
                <i class="icon bottom-nav__icon bottom-nav__icon-member"></i>
                <p>会员</p>
            </a>
            <a href="setting.html" :class="{curr:isManage}">
                <i class="icon bottom-nav__icon bottom-nav__icon-manage"></i>
                <p>管理</p>
            </a>
        </div>
    </div>
</template>
<script>
    export default {
        name : 'maFooter',
        props : {
            current : {
                type : String
            }
        },
        computed : {
            isIndex(){
                return this.current === "index"
            },
            isOrder(){
                return this.current === "order"
            },
            isMember(){
                return this.current === "member"
            },
            isManage(){
                return this.current === "manage"
            }
        }
    };
</script>
<style scoped>
    .bottom-nav__wrap {
        position: fixed;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 10;
        height: 48px;
        background-color: #fff;
        border-top: 1px solid #e5e5e5;
    }
    .bottom-nav {
        max-width: 640px;
        height: 100%;
        margin: 0 auto;
    }
    .bottom-nav a {
        float: left;
        width: 25%;
        padding: 5px 0;
        font-size: 10px;
        line-height: 1;
        color: #494949;
        text-align: center;

    }
    .bottom-nav .curr {
        color: #1daae5;
    }
    .bottom-nav__icon {
        display: block;
        width: 22px;
        height: 22px;
        margin: 0 auto 7px;
        background-size:cover;
    }
    .bottom-nav__icon-home {
        background-image:url('../assets/index.png')
    }
    .curr .bottom-nav__icon-home {
        background-image:url('../assets/index_curr.png')
    }
    .bottom-nav__icon-order {
        background-image:url('../assets/order.png')
    }
    .curr .bottom-nav__icon-order {
        background-image:url('../assets/order_curr.png')
    }
    .bottom-nav__icon-member {
        background-image:url('../assets/member.png')
    }
    .curr .bottom-nav__icon-member {
        background-image:url('../assets/member_curr.png')
    }
    .bottom-nav__icon-manage {
        background-image:url('../assets/manage.png')
    }
    .curr .bottom-nav__icon-manage {
        background-image:url('../assets/manage_curr.png')
    }

</style>