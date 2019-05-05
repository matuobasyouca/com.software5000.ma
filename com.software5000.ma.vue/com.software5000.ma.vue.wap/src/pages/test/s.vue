<template>
    <div>123123123</div>
</template>
<script>
    export default {
        methods: {
            setWxConfig(fn, loading) {
                this.$ajax(
                    this.$joggle.customer.paySuccess.getWxConfig,
                    { url: window.location.href },
                    loading,
                    (data) => {
                        if (data.code === 'ZS011000') {
                            fn && fn();
                            wx.config({
                                debug: false,
                                appId: data.data.appId,
                                timestamp: data.data.timestamp,
                                nonceStr: data.data.nonce,
                                signature: data.data.signature,
                                jsApiList: ['checkJsApi', 'onMenuShareTimeline', 'onMenuShareAppMessage']
                            });
                            wx.ready(() => {
                                wx.getLocation({
                                    type: 'wgs84',
                                    success(res) {
                                        const latitude = res.latitude;
                                        const longitude = res.longitude;
//                                        const speed = res.speed; // 速度，以米/每秒计
//                                        const accuracy = res.accuracy; // 位置精度

                                        alert(latitude,longitude)
                                    }
                                });
                            });
                        } else {
                            loading.close();
                            this.$message({
                                type: 'error',
                                message: data.msg,
                                duration: 1200
                            });
                        }
                    }
                );
            }
        },
        created(){
            const loading = this.$loading();

            this.setWxConfig(() => {
                loading.close();
            }, loading);
        }
    };
</script>