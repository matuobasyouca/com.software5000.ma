webpackJsonp([1],{137:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"swiperBanner",props:{images:{type:Array,default:function(){return[]}},indexDefault:{type:[Number,String],default:0},speed:{type:[Number,String],default:2500},value:{type:Boolean,default:!0}},data:function(){return{winW:0,swiperHeight:"280px",startX:0,startY:0,endX:0,endY:0,imageIndex:0,auto:""}},computed:{imageListStyle:function(){var t={};return t.width=this.images.length*this.winW+"px",t.left="-"+this.imageIndex*this.winW+"px",this.swiperHeight=.6*this.winW+"px",t},imageStyle:function(){var t={};return t.width=this.winW+"px",t}},watch:{value:function(t){t&&(this.imageIndex=this.index||0)}},methods:{handImgAutoTurn:function(){var t=this;this.auto=setInterval(function(){t.imageIndex==t.images.length-1?t.imageIndex=0:t.imageIndex++},this.speed)},handleMoveStart:function(t){clearInterval(this.auto),this.startX=t.touches[0].pageX,this.startY=t.touches[0].pageY},handleMoveEnd:function(t){switch(this.endX=t.changedTouches[0].pageX,this.endY=t.changedTouches[0].pageY,this.getDirection(this.startX,this.startY,this.endX,this.endY)){case 0:break;case-1:this.imageIndex<this.images.length-1&&this.imageIndex++;break;case 1:this.imageIndex>0&&this.imageIndex--}this.handImgAutoTurn()},handImgClick:function(t,e){this.$emit("click",e)},getDirection:function(t,e,a,s){var i=e-s,n=a-t,o=0;if(Math.abs(n)<5)return o;var r=this.getAngle(n,i);return r>=-45&&r<45?o=1:r>=45&&r<135||r>=-135&&r<-45?o=0:(r>=135&&r<=180||r>=-180&&r<-135)&&(o=-1),o},getAngle:function(t,e){return 180*Math.atan2(e,t)/Math.PI},backgroundImage:function(t){var e={};return e.backgroundImage="url("+t+")",e}},created:function(){this.winW=document.body.clientWidth,this.handImgAutoTurn()}}},148:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=a(6),i=a.n(s),n=a(213),o=a.n(n),r=a(2),c=a(7),u=a(10),d=a(8);e.default={components:{maHead:i.a,swiperBanner:o.a},data:function(){return{openId:"",groupOpenId:"",packCulsterId:"",isMySelf:!0,openState:0,packCulsterData:{businessPackage:{}},openRecordId:"",openCulsterData:{packCluster:{},packClusterPerDtos:[]},description:"",culsterList:[],wxCode:"",images:[],countdown:0,activityState:0,groupState:0,shareModel:!1,shareDescription:"",attachedText:"",businessPackage:[],packageAndItems:[],businessName:""}},computed:{handCountdownH:function(){var t=parseInt(this.countdown/60/60);return(t=t>9?t:"0"+t)||"00"},handCountdownM:function(){var t=parseInt(this.countdown/60%60);return(t=t>9?t:"0"+t)||"00"},handCountdownS:function(){var t=this.countdown%60;return(t=t>9?t:"0"+t)||"00"},handDescriptionText:function(){var t="";if(!a.i(r.c)(this.description)){this.description.split("\n").forEach(function(e){t+="<li>"+e+"</li>"})}return t}},watch:{},methods:{getData:function(t,e){var a=this;this.$ajax(this.$joggle.customer.packcluster.selectPackClusterInfo,{id:this.packCulsterId},e,function(e){if("ZS011000"===e.code){a.packCulsterData=e.data,a.shareDescription=e.data.shareDescription,a.attachedText=decodeURIComponent(e.data.attachedText),a.description=e.data.description,a.images=e.data.packClusterImgs,a.handActivityState(a.packCulsterData.beginDay,a.packCulsterData.endDay);var s=new Promise(function(t){a.handleBusinessName(function(){t()},a.packCulsterData.packId)}),i=new Promise(function(t){a.handleBusinessId(function(){t()},a.packCulsterData.businessId)});Promise.all([s,i]).then(function(){t&&t()})}else t&&t(),a.$message({type:"error",message:e.msg})})},handleBusinessName:function(t,e){var a=this;this.$ajax(this.$joggle.customer.packcluster.selectBusinessPackageForOpenEmkt,{packIds:e},!0,function(s){"ZS011000"===s.code?(a.businessPackage=s.data[e],a.packageAndItems=s.data[e].packageAndItems):a.$message({type:"error",message:s.msg}),t&&t()})},handleBusinessId:function(t,e){var a=this;this.$ajax(this.$joggle.customer.packcluster.selectEnableBusinessNameList,{businessIds:e+""},!0,function(s,i){"ZS011000"===s.code?(a.businessName=s.data[e].businessName,document.title=a.businessName||"门店"):(i.close(),a.$message({type:"error",message:s.msg,duration:"1000"})),t&&t()})},getGroupData:function(t,e){var a=this;this.$ajax(this.$joggle.customer.packcluster.selectPackClusterPeron,{buyRecordId:this.openRecordId},e,function(e){if("ZS011000"===e.code){a.openCulsterData=e.data;var s=e.data.packClusterPerDtos;s.sort(function(t,e){var a=t.header?1:0;return(e.header?1:0)-a}),a.culsterList=s,a.isMySelf=JSON.stringify(s).indexOf(a.openId)>-1,a.openState=e.data.openState;var i=r.g.dateToRegular(e.data.serviceTime).getTime(),n=r.g.dateToRegular(e.data.openCreateTime).getTime()+1e3*e.data.packCluster.clusterHour*60*60;a.handRemainingTime(i,n)}else a.$message({type:"error",message:e.msg});t&&t()})},handGetOpenId:function(){var t=this,e=this.$loading(),a=new Promise(function(a){t.certJoinGroup(function(){t.openRecordId?t.getGroupData(function(){a()},e):a()},e)}),s=new Promise(function(a){t.getData(function(){a()},e)});Promise.all([s,a]).then(function(){t.openRecordId?(t.isMySelf?(t.activityState=t.openState+2,1==t.openState&&0==t.countdown&&(t.activityState=5)):2==t.packCulsterData.state&&(t.activityState=2),t.groupState=1==t.openState?0:1,1==t.openState&&0==t.countdown&&(t.groupState=1)):t.activityState=2==t.packCulsterData.state?2:t.activityState,t.setWxConfig(function(){e.close()})})},setWxConfig:function(t){var e=this;this.$ajax(this.$joggle.open.selectWxJsAPI,{url:window.location.href},!1,function(a){if("ZS011000"===a.code){var s=window.location.host,i=e.shareDescription,n=e.packCulsterData.shareImgPath?e.packCulsterData.shareImgPathUrl:e.packCulsterData.packClusterImgs[0].imgPathUrl,o=e.packCulsterData.clusterName,r="http://"+s+"/wap/customer/open/group/activity.html?id="+e.packCulsterId;e.openRecordId&&(o="【仅剩"+(e.openCulsterData.packCluster.clusterNum-e.openCulsterData.packClusterPerDtos.length)+"个名额】 "+e.packCulsterData.clusterName,r="http://"+s+"/wap/customer/open/group/activity.html?id="+e.packCulsterId+"&openRecordId="+e.openRecordId),wx.config({debug:!1,appId:a.data.appId,timestamp:a.data.timestamp,nonceStr:a.data.nonce,signature:a.data.signature,jsApiList:["checkJsApi","onMenuShareTimeline","onMenuShareAppMessage"]}),wx.ready(function(){wx.onMenuShareTimeline({title:o,desc:i,link:r,imgUrl:n,type:"link"}),wx.onMenuShareAppMessage({title:o,desc:i,link:r,imgUrl:n,type:"link"})}),t&&t()}else e.$message({type:"error",message:a.msg,duration:1200})})},certJoinGroup:function(t,e){var s=this;this.$ajax(this.$joggle.customer.packcluster.selectHaveBuyPackCluster,{carNumber:"",mobile:"",packClusterId:this.packCulsterId,wxOpenId:this.openId},e,function(e){"ZS012030"===e.code&&(s.openRecordId&&s.openRecordId!=e.data?(s.$message({type:"error",message:"您已开过团"}),setTimeout(function(){var t={id:s.packCulsterId,openRecordId:e.data};a.i(r.d)("/wap/customer/open/group/activity.html",t)},1e3)):s.openRecordId=e.data),t&&t()})},handWeChatImg:function(t){var e="";return t&&(e=t.wxHeadImg?"background-image: url("+t.wxHeadImg+")":"background-image: url(/wap/static/img/user-default.0f04b42.jpg)"),e},handRemainingTime:function(t,e){var a=this,s=e;if(s>t){this.countdown=parseInt((s-t)/1e3);var i=setInterval(function(){a.countdown>0?a.countdown--:(clearInterval(i),a.isMySelf?a.activityState=5:a.groupState=1)},1e3)}},handActivityState:function(t,e){var a=(new Date).getTime(),s=r.g.dateToRegular(t).getTime(),i=r.g.dateToRegular(e).getTime();a<s?this.activityState=1:a>i&&(this.activityState=2)},handOpenGroup:function(){if(0==this.activityState&&2!=this.packCulsterData.state){var t={i:d.a.encode(this.openId),id:this.packCulsterId};a.i(r.d)("/wap/customer/open/group/join.html",t)}else 1==this.openState&&(this.shareModel=!0)},handOpenGroup2:function(t){var e={i:d.a.encode(this.openId),id:this.packCulsterId};if(1==t){if(0!=this.activityState&&5!=this.activityState&&2==this.packCulsterData.state)return}else if(2==t){if(1==this.groupState)return;e.openRecordId=this.openCulsterData.openRecordId}a.i(r.d)("/wap/customer/open/group/join.html",e)}},mounted:function(){var t=this;if(console.log(),this.packCulsterId=a.i(r.a)("id")?parseInt(a.i(r.a)("id")):"",this.openRecordId=a.i(r.a)("openRecordId")?parseInt(a.i(r.a)("openRecordId")):"",this.shareModel=!!a.i(r.a)("showShare"),this.packCulsterId||(this.$message({type:"error",message:"请从正常路径进入页面"}),setTimeout(function(){a.i(c.a)("/wap/open/customer/detail_user.html","emkt")},1e3)),window.sessionStorage.i)this.openId=window.sessionStorage.i,this.handGetOpenId();else{var e=this.$loading();a.i(u.a)(function(s){t.wxCode=s,a.i(r.c)(s)||a.i(u.b)(s,function(e){window.sessionStorage.i=e,t.openId=e,t.handGetOpenId()},e)})}}}},213:function(t,e,a){a(397);var s=a(0)(a(137),a(327),null,null);t.exports=s.exports},223:function(t,e,a){a(382);var s=a(0)(a(148),a(310),null,null);t.exports=s.exports},310:function(t,e,a){t.exports={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"activity"},[s("swiper-banner",{attrs:{images:t.images}}),t._v(" "),s("div",{staticClass:"contain"},[s("div",{staticClass:"price-position"},[s("div",{staticClass:"price-left"},[s("span",{staticClass:"title"},[t._v("拼团价")]),s("span",{staticClass:"red"},[t._v("￥"+t._s(t.packCulsterData.salePrice||"0"))]),s("i",{staticClass:"del"},[t._v("原价：￥"+t._s(t.businessPackage?t.businessPackage.salePrice:"0"))])])]),t._v(" "),s("div",{staticClass:"item group-card"},[s("div",{staticClass:"price"},[s("div",{staticClass:"price-right"},[t._v(t._s(t.packCulsterData.clusterNum)+"人团")])]),t._v(" "),s("div",{staticClass:"title"},[t._v("\n                "+t._s(t.packCulsterData.clusterName)+"\n            ")]),t._v(" "),t.businessPackage?s("div",{staticClass:"card"},[s("span",[t._v(t._s(t.businessPackage.packageName))]),t._v(" "),s("div",{staticClass:"r"},[s("zs-icon",{attrs:{size:12,icon:"time3",text:0!=t.businessPackage.validTerm?"有效期："+t.businessPackage.validTerm+"年":"有效期：永久"}})],1)]):t._e(),t._v(" "),s("div",{staticClass:"card-item"},[s("ul",[t._l(t.packageAndItems,function(e,a){return s("li",[s("span",{staticClass:"h3"},[t._v(t._s(e.serviceItem.itemName))]),t._v(" "),s("span",{staticClass:"time"},[t._v(t._s(e.useTimes)+"次")])])}),t._v(" "),t.packageAndItems&&t.packageAndItems.length%2!=0?s("li",[s("span",{staticClass:"h3"}),t._v(" "),s("span",{staticClass:"time"})]):t._e()],2)]),t._v(" "),s("div",{staticClass:"notice"},[t._v("\n                拼团须知\n                "),s("div",{staticClass:"ri"},[s("zs-icon",{attrs:{size:16,icon:"user3",text:"好友拼单"}}),t._v(" "),s("zs-icon",{attrs:{size:16,icon:"check3",text:"人满成功"}}),t._v(" "),s("zs-icon",{attrs:{size:16,icon:"cash-pay2",text:"人不满退款"}})],1)])]),t._v(" "),t.openRecordId?s("div",{staticClass:"item member"},[0!=t.culsterList.length?s("ul",{staticClass:"member-top"},t._l(t.openCulsterData.packCluster.clusterNum,function(e,a){return s("li",{key:a,class:{on:t.culsterList[a]},style:t.handWeChatImg(t.culsterList[a])})})):t._e(),t._v(" "),t.countdown&&1==t.openState?s("div",{staticClass:"member-bottom"},[t._v("\n                仅剩下 "),s("span",{staticClass:"red"},[t._v(t._s(t.openCulsterData.packCluster.clusterNum-t.openCulsterData.packClusterPerDtos.length)+"  ")]),t._v("\n                个名额，\n                "),s("div",{staticClass:"countdown"},[s("i",{staticClass:"red-block"},[t._v(t._s(t.handCountdownH))]),t._v(" "),s("span",[t._v(":")]),t._v(" "),s("i",{staticClass:"red-block"},[t._v(t._s(t.handCountdownM))]),t._v(" "),s("span",[t._v(":")]),t._v(" "),s("i",{staticClass:"red-block"},[t._v(t._s(t.handCountdownS))])]),t._v("\n                后结束\n            ")]):s("div",{staticClass:"member-bottom"},[t._v("\n                已结束\n            ")])]):t._e(),t._v(" "),s("div",{staticClass:"item item-bottom"},[s("div",{staticClass:"group-info-top"},[t._v("拼团说明")]),t._v(" "),t.handDescriptionText?s("ul",{staticClass:"group-info-main",domProps:{innerHTML:t._s(t.handDescriptionText)}}):s("p",{staticClass:"group-none"},[t._v("暂无拼团说明")])]),t._v(" "),s("div",{staticClass:"item item-bottom"},[s("div",{staticClass:"group-info-top"},[t._v("商品详情")]),t._v(" "),t.attachedText?s("div",{staticClass:"group-detail",domProps:{innerHTML:t._s(t.attachedText)}}):s("p",{staticClass:"group-none"},[t._v("暂无商品详情")])])]),t._v(" "),t.shareModel?s("div",{staticClass:"share-model",on:{click:function(e){t.shareModel=!1}}},[s("div",{staticClass:"share-body"},[s("zs-icon",{staticClass:"guide-arrow-icon",attrs:{icon:"guide-arrow",size:"100"}}),t._v(" "),s("div",{staticClass:"body-main"},[s("p",[t._v("还差"+t._s(t.openCulsterData.packCluster.clusterNum-t.openCulsterData.packClusterPerDtos.length)+"人")]),t._v(" "),s("p",[t._v("点击右上角发送给朋友")])])],1)]):t._e(),t._v(" "),s("div",{staticClass:"page-footer"},[t.isMySelf?s("div",{staticClass:"footer-body self",class:{gray:0!=t.activityState&&3!=t.activityState&&4!=t.activityState},on:{click:t.handOpenGroup}},[t._v("\n            "+t._s(["一键开团","活动未开始","活动已结束","您已参与该拼团,邀请好友拼单","您的拼团已成功","拼团失败，正在退款"][t.activityState])+"\n        ")]):s("div",{staticClass:"footer-body clr"},[0==t.activityState||3==t.activityState?s("div",{staticClass:"footer-body-left",on:{click:function(e){t.handOpenGroup2(1)}}},[t._v("\n                我也来开团\n            ")]):t._e(),t._v(" "),s("div",{staticClass:"footer-body-right",class:{gray:1!=t.openState||1==t.groupState,w100:0!=t.activityState&&3!=t.activityState},on:{click:function(e){t.handOpenGroup2(2)}}},[t._v("\n                "+t._s(["一键参团","拼团已结束","拼团失败，正在退款"][t.groupState])+"\n            ")])])]),t._v(" "),s("img",{staticStyle:{display:"none"},attrs:{src:a(419),alt:""}})],1)},staticRenderFns:[]}},327:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"image-view-box",style:{height:t.swiperHeight}},[a("ul",{staticClass:"image-view-list",style:t.imageListStyle,on:{touchstart:function(e){e.stopPropagation(),t.handleMoveStart(e)},touchend:function(e){e.stopPropagation(),t.handleMoveEnd(e)}}},t._l(t.images,function(e,s){return a("li",{key:e.id,style:[t.imageStyle,t.backgroundImage(e.imgPathUrl)],on:{click:function(a){t.handImgClick(e,s)}}})})),t._v(" "),a("p",{staticClass:"image-view-index"},[t._v(t._s(t.imageIndex+1+" / "+t.images.length))])])},staticRenderFns:[]}},382:function(t,e){},397:function(t,e){},419:function(t,e,a){t.exports=a.p+"static/img/user-default.0f04b42.jpg"},454:function(t,e,a){t.exports=a(51)},51:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=a(1),i=a(223),n=a.n(i),o=a(5),r=(a.n(o),a(3),a(4));s.a.use(r.a),new s.a({el:"#app",render:function(t){return t(n.a)}})}},[454]);