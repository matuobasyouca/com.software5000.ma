webpackJsonp([5],{181:function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=t(2),i=t(12),o=t(6),c=t.n(o),n=t(9),r=t.n(n),l=t(30),u=t.n(l),d=t(29),p=t.n(d);a.default={components:{maHead:c.a,carNumber:r.a,upload:u.a,swiper:p.a},data:function(){return{isLoading:!0,isOperator:!1,state:"",canSubmit:!0,orderTimer:null,memberRecordId:"",memberLvl:"",packageRemain:{},remarks:"",useBalance:!0,memberBalanceDeduct:0,itemSelectDatas:[],images:[],imageViewVisible:!1,imageIndex:0,orderNo:"",createTime:"",commitTime:"",payTime:"",_totalPrice:0,payCashPopVisible:!1,payWechatPopVisible:!1,finalData:{id:"",totalPrice:"",carNumber:"",workOrderDetails:[],userId:"",user:{realName:""},mobile:"",businessDeduct:"",materialCost:"",payType:"",comment:""},couponSelectVisible:!1,couponsData:[],couponsItemData:[],couponKey:1,currItemIndex:"",currItemType:"",items:[],_discountNum:10,_discountDeduct:0,_couponDeduct:0,tempCouponShow:[]}},computed:{packageDeduct:function(){var e=this,a=0;return this.itemSelectDatas.forEach(function(t){4==e.state?a+=(t.usePackageTimes||0)*t.salePrice:a+=(t._useCard?t.usePackageTimes:0)*t.salePrice}),parseFloat(a).toFixed(2)},itemCalcTotalPrice:function(){var e=this,a=0;return this.itemSelectDatas.forEach(function(t){e.isOperator?a+=parseFloat(t.itemsTimes*t.salePrice):a+=parseFloat(t.totalPrice)}),parseFloat(a).toFixed(2)},priceBeforeBusinessDeduct:function(){return parseFloat(this.itemCalcTotalPrice)+parseFloat(this.finalData.materialCost||0)},totalPriceBeforeBalance:function(){var e=this.priceBeforeBusinessDeduct-(this.finalData.businessDeduct||0);return parseFloat(e>0?e:0).toFixed(2)},realTotalPrice:function(){var e=this.totalPriceBeforeBalance-(this.useBalance?this.memberBalanceDeduct:0);return parseFloat(e>0?e:0).toFixed(2)}},methods:{insertWorkOrderImg:function(e,a,t){var s=this;this.$ajax(this.$joggle.merchant.workorder.insertWorkOrderImg,{workOrderId:this.finalData.id,orderImg:e},t,function(e,t){"ZS011000"===e.code?a&&a(e.data):(t.close(),s.$message({type:"error",message:e.msg}))})},deleteWorkOrderImg:function(e,a){var t=this;this.$ajax(this.$joggle.merchant.workorder.deleteWorkOrderImg,{imgId:a},!0,function(a,s){s.close(),"ZS011000"===a.code?(t.$delete(t.images,e),t.$message({type:"success",message:"删除成功",duration:1e3})):t.$message({type:"error",message:a.msg,duration:1e3})})},deleteOrder:function(){var e=this;this.$ajax(this.$joggle.merchant.workorder.deleteWorkOrderById,{orderId:this.finalData.id},!0,function(a,t){t.close(),"ZS011000"===a.code?(e.$message({type:"success",message:"取消成功"}),setTimeout(function(){window.history.go(-1)},1200)):e.$message({type:"error",message:a.msg})})},updateWorkOrderForFinish:function(){var e=this;this.$ajax(this.$joggle.merchant.workorder.updateWorkOrderForFinish,{orderId:this.finalData.id},!0,function(a,t){"ZS011000"===a.code?e.selectWorkOrder(function(){e.$message({type:"success",message:a.msg}),t.close()},t):(t.close(),e.$message({type:"error",message:a.msg}))})},updateWorkOrderForSettle:function(){var e=this;this.$ajax(this.$joggle.merchant.workorder.updateWorkOrderForSettle,{orderId:this.finalData.id},!0,function(a,i){i.close(),"ZS011000"===a.code?(e.$message({type:"success",message:"收款成功"}),e.payCashPopVisible=!1,setTimeout(function(){t.i(s.d)("/wap/merchant/home/workorder/detail.html",{id:e.finalData.id})},1200)):e.$message({type:"error",message:"收款失败"})})},updateWorkOrder:function(e,a){var t=this;this.finalData.businessDeduct=1*Math.min(parseFloat(this.finalData.businessDeduct||0),this.priceBeforeBusinessDeduct).toFixed(2),this.finalData.couponDeduct=parseFloat(this.realCouponDeduct||0),this.finalData.discountDeduct=parseFloat(this.discountDeduct||0),this.finalData.materialCost=1*parseFloat(this.finalData.materialCost||0).toFixed(2),this.finalData.balanceDeduct=this.useBalance?Math.min(parseFloat(this.memberBalanceDeduct),parseFloat(this.totalPriceBeforeBalance)).toFixed(2):0,this.finalData.totalPrice=parseFloat(this.realTotalPrice),this.itemSelectDatas.forEach(function(e){e.usePackageTimes=e._useCard?e.usePackageTimes:0}),this.finalData.workOrderDetails=this.itemSelectDatas,this.$ajax(this.$joggle.merchant.workorder.updateWorkOrder,this.finalData,a,function(a,s){"ZS011000"===a.code?e&&e():(s.close(),t.$message({type:"error",message:a.msg,duration:1200}))})},selectWorkOrder:function(e,a){var i=this;this.$ajax(this.$joggle.merchant.workorder.selectWorkOrderById,{orderId:this.finalData.id},a,function(a,o){if("ZS011000"===a.code){var c=a.data.workOrder,n=a.data.user;if(t.i(s.c)(c))return i.$message({type:"error",message:"工单不存在!",duration:1200}),o.close(),void(i.canSubmit=!1);i.packageRemain=i.handlePackageMerge(t.i(s.c)(n)?[]:n.packageList),i.state=c.state,i.finalData.carNumber=c.carNumber,i.finalData.id=c.id,i.finalData.mobile=c.mobile,i.finalData.businessDeduct=c.businessDeduct||"",i.finalData.materialCost=c.materialCost||"",i.finalData.comment=c.comment||"",i.finalData.payType=c.payType||"",i.orderNo=c.orderNo||"",i.createTime=c.createTime||"",i.commitTime=c.commitTime||"",i.payTime=c.payTime||"",i._totalPrice=c.totalPrice||0,i.finalData.user.realName=t.i(s.c)(n)?"":n.realName,i.finalData.userId=t.i(s.c)(n)?"":n.userId||"",i.memberRecordId=t.i(s.c)(n)?"":n.recordId||"",i.memberLvl=t.i(s.c)(i.memberRecordId)?"非会员":n.lvlName||"普通会员",i.remarks=t.i(s.c)(n)?"":n.remarks||"",i.useBalance=c.balanceDeduct>0,i.memberBalanceDeduct=3==i.state?t.i(s.c)(n)?0:n.memberBalance||0:c.balanceDeduct||0;var r=c.workOrderImgs||[],l=[];r.forEach(function(e){l.push({id:e.id,showImg:e.orderImgSrc})}),i.images=l;var u=c.workOrderDetails||[];if(3==i.state){var d=new Promise(function(e){i.selectCouponsByCarNumber(function(){e()},o)}),p=new Promise(function(e){i.selectServiceItem(function(){e()},o)});Promise.all([d,p]).then(function(){u.forEach(function(e){i.items.forEach(function(a){e.serviceItemId===a.id&&(i.$set(e,"discountNumber",a.discountNumber),i.$set(e,"discountType",a.discountType))}),i.$set(e,"_hasCard",!1),i.$set(e,"_useCard",!1),i.$set(e,"_autoCoupon",!0),(t.i(s.c)(e.itemType)||t.i(s.c)(e.serviceItemId))&&(e.itemType=-1);for(var a in i.packageRemain)if(i.packageRemain[a]>0&&a==e.serviceItemId){i.$set(e,"_hasCard",!0),i.$set(e,"_useCard",parseInt(e.usePackageTimes||0)>0),i.$set(e,"_autoCoupon",0===parseInt(e.usePackageTimes||0)),i.$set(e,"usePackageTimes",Math.min(e.itemsTimes,i.packageRemain[a]));break}i.$set(e,"couponKey",i.couponKey);var o=!1;t.i(s.c)(e.couponUuid)||(i.couponsData.forEach(function(a){e.couponUuid===a.takeUuid&&(i.$set(a,"couponKey",i.couponKey),o=!0)}),o||(i.$set(e,"couponUuid",""),i.$set(e,"couponName",""),i.$set(e,"couponDeduct",""))),i.couponKey++}),e&&e()})}else 1==i.state&&u.forEach(function(e){i.$set(e,"_hasCard",i.packageRemain[e.serviceItemId]>0),i.$set(e,"_useCard",parseInt(e.usePackageTimes||0)>0)}),e&&e();i.itemSelectDatas=u,i._discountNum=c.discountNum||10,i._discountDeduct=c.discountDeduct||0,i._couponDeduct=c.couponDeduct||0;for(var m=c.couponName?c.couponName.split(","):[],h=c.couponEveryDeduct?c.couponEveryDeduct.split(","):[],f=0,_=m.length;f<_;f++)i.tempCouponShow.push({couponName:m[f],couponDeduct:h[f]})}else o.close(),i.$message({type:"error",message:a.msg,duraton:1200})},function(){i.finalData.id="",i.finalData.totalPrice=0,i.finalData.workOrderDetails=[],i.finalData.userId="",i.finalData.user.realName="",i.finalData.mobile="",i.finalData.businessDeduct="",i.finalData.materialCost="",i.finalData.comment="",i.itemSelectDatas=[],i.memberLvl="",i.packageRemain={},i.images=[]})},selectServiceItem:function(e,a){var t=this;this.$ajax(this.$joggle.merchant.workorder.selectServiceItemByPage,{itemType:"",startPage:1,pageSize:999,userId:this.finalData.userId},a,function(a){"ZS011000"===a.code?t.items=a.data.list:t.$message({type:"error",message:a.msg}),e&&e()})},selectCouponsByCarNumber:function(e){var a=this;this.$ajax(this.$joggle.merchant.workorder.selectCouponsByCarNumber,{carNumber:this.finalData.carNumber},!0,function(t,s){if(s.close(),"ZS011000"===t.code){var i=t.data||[];i.forEach(function(e){a.$set(e,"_selected",!1)}),a.couponsData=i,e&&e()}else a.$message({type:"error",message:t.msg,duration:1200})})},selectImageLink:function(e){var a=this;this.$ajax(this.$joggle.mzscp.uploadImgForBase64,{linkType:"workOrderImg",img:e},!0,function(e,t){"ZS011000"===e.code?a.insertWorkOrderImg(e.data.imgLink,function(e){a.images.push({id:e.id,showImg:e.orderImgSrc}),a.$message({type:"success",message:"上传成功"}),t.close()},t):a.$message({type:"error",message:e.msg})})},selectCouponsById:function(e,a){var i=this;if(t.i(s.c)(this.finalData.couponUuid))return void(e&&e());this.$ajax(this.$joggle.merchant.workorder.selectCouponsById,{uuid:this.finalData.couponUuid},a,function(a,t){"ZS011000"===a.code?e&&e():(t.close(),i.$message({type:"error",message:a.msg}))})},selectIsPaySuccess:function(e,a){var t=this;this.$ajax(this.$joggle.merchant.workorder.selectWorkOrderById,{orderId:this.finalData.id},!1,function(s){"ZS011000"===s.code?4===parseInt(s.data.workOrder.state)?e&&e():a&&a():t.$message({type:"error",message:s.msg})})},handleGoHome:function(){t.i(s.d)("/wap/merchant/home/manage/index.html")},handleToMemberDetail:function(){t.i(s.c)(this.memberRecordId)||this.isOperator||t.i(s.d)("/wap/merchant/home/member/detail.html",{id:this.memberRecordId})},handleViewPic:function(e){this.imageViewVisible=!0,this.imageIndex=e},handlePackageMerge:function(e){var a={1:0};if(e.length>0)for(var t=0;t<e.length;t++)if(e[t].memberItemUseRecords.length>0)for(var s=e[t].memberItemUseRecords,i=0;i<s.length;i++){var o=!0;for(var c in a)if(parseInt(c)===parseInt(s[i].serviceItem.id)){a[c]=parseInt(a[c])+parseInt(s[i].remainTimes),o=!1;break}o&&(a[s[i].serviceItem.id]=s[i].remainTimes)}return a},handleToggleUseCard:function(e){var a=this,t=this.itemSelectDatas[e];if(this.$set(t,"_autoCoupon",!1),t._useCard){this.$set(t,"couponUuid",""),this.$set(t,"couponName",""),this.$set(t,"couponDeduct",0);for(var s=0;s<this.couponsData.length;s++)if(a.couponsData[s].couponKey===t.couponKey){a.$set(a.couponsData[s],"couponKey","");break}}},handleDiscountShow:function(e){return 3==this.state&&e.discountType&&(e.discountNumber||"0"==e.discountNumber)||e.discountPrice>0},handleCalcDiscountPrice:function(e,a){var t=0,s=(e.itemsTimes-(e._useCard?e.usePackageTimes:0))*e.salePrice-(e.couponDeduct||0);if(1==e.discountType)t=parseFloat((1-e.discountNumber/10)*s).toFixed(2);else if(2==e.discountType){var i=(e.itemsTimes-(e._useCard?e.usePackageTimes:0))*(e.salePrice-e.discountNumber);t=parseFloat(i-s>0?s:i).toFixed(2)}return this.$set(this.itemSelectDatas[a],"discountPrice",parseFloat(t)),t},handleCalcItemTotalPrice:function(e,a){var t=parseFloat((e.itemsTimes-(e._useCard?e.usePackageTimes:0))*e.salePrice-(e.couponDeduct||0)-e.discountPrice).toFixed(2);return this.$set(this.itemSelectDatas[a],"totalPrice",parseFloat(t)),t},handlePopCoupon:function(e,a){var t=this;if(3==this.state){if(this.itemSelectDatas[a]._useCard)return void this.$message({type:"warning",message:"已选择使用套餐，不能使用优惠券！"});this.currItemIndex=a,this.currItemType=e||"-1";var s=[];this.couponsData.forEach(function(e,a){t.handleCouponListShow(e)&&(e._index=a,s.push(e))}),this.couponsItemData=s,this.couponsItemData.length>0&&(this.couponSelectVisible=!0)}},handleCouponListShow:function(e,a){a=t.i(s.c)(a)?this.currItemIndex:a;var i=this.itemSelectDatas[a],o=i.itemType;return!t.i(s.c)(i)&&((!e.couponKey||e.couponKey==i.couponKey)&&(!e.coupon.itemType||e.coupon.itemType==o)&&e.coupon.cpMoney-i.itemsTimes*i.salePrice<=0)},handleSelectCoupon:function(e){var a=this,t=this.itemSelectDatas[this.currItemIndex];this.$set(t,"_autoCoupon",!1);for(var s=0;s<this.couponsData.length;s++)if(a.couponsData[s].takeUuid===e){a.couponsData[s].couponKey===t.couponKey?(a.$set(a.couponsData[s],"couponKey",""),a.$set(t,"couponUuid",""),a.$set(t,"couponName",""),a.$set(t,"couponDeduct",0)):(a.couponsItemData.forEach(function(e){a.$set(e,"couponKey",""),a.$set(a.couponsData[e._index],"couponKey","")}),a.$set(a.couponsData[s],"couponKey",t.couponKey),a.$set(t,"couponUuid",a.couponsData[s].takeUuid),a.$set(t,"couponName",a.couponsData[s].coupon.cpName),a.$set(t,"couponDeduct",a.couponsData[s].coupon.cpMoney));break}this.couponSelectVisible=!1},handleCouponShow:function(e,a){var t=this,s=[];return this.couponsData.forEach(function(e){t.handleCouponListShow(e,a)&&s.push(e)}),0===s.length&&3==this.state?"暂无可用卡券":e.couponName?"-￥"+e.couponDeduct:"请选择卡券"},handleCashPay:function(){var e=this;if(this.canSubmit){this.finalData.payType=1;var a=this.$loading();this.selectCouponsById(function(){e.updateWorkOrder(function(){e.payCashPopVisible=!0,a.close()},a)},a)}},handleWechatPay:function(){var e=this;if(this.canSubmit&&0!==parseFloat(this.realTotalPrice)){this.finalData.payType=2;var a=this.$loading();this.selectCouponsById(function(){e.updateWorkOrder(function(){e.payWechatPopVisible=!0,e.handleShowBarCode(),a.close()},a)},a)}},handleDeleteOrderClick:function(){var e=this;this.$confirm({type:"order",message:"是否取消该工单?"}).then(function(){e.deleteOrder()}).catch(function(){})},handleShowBarCode:function(){var e=this;t.i(s.n)(this.$joggle.merchant.workorder.weChatPayBarCode+"?orderId="+this.finalData.id+"&totalPrice="+this.finalData.totalPrice+"&t="+(new Date).getTime(),function(a){e.$refs.barCodeWrap.innerHTML="",e.$refs.barCodeWrap.appendChild(a),clearInterval(e.orderTimer),e.orderTimer=setInterval(function(){e.selectIsPaySuccess(function(){e.payWechatPopVisible=!1,e.$message({type:"success",message:"支付成功"}),setTimeout(function(){t.i(s.d)("/wap/merchant/home/workorder/detail.html",{id:e.finalData.id})},1200)})},2e3)})}},created:function(){var e=this;if(this.finalData.id=t.i(s.a)("id")||"",t.i(s.c)(this.finalData.id))return this.$message({type:"error",message:"该工单不存在"}),void(this.canSubmit=!1);var a=this.$loading();t.i(i.a)(function(t){e.isOperator=!!t.mercharType&&t.mercharType.indexOf("operator")>-1,e.selectWorkOrder(function(){a.close(),e.isLoading=!1},a)})}}},25:function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=t(13);a.default={name:"UploadPhoto",props:{reset:Boolean,images:{type:Array,default:function(){return[]}},max:{type:[Number,String],default:6}},data:function(){return{}},computed:{isFull:function(){return this.images.length>=this.max}},methods:{handlePushImg:function(e){var a=this;t.i(s.a)(e.target,600,function(t){a.isFull||(a.$emit("change",t.slice(23)),e.target.value="")})},handleDelImg:function(e,a){var t=this;this.$confirm({type:"delete2",message:"确认删除该图片？"}).then(function(){t.$emit("delete",e,a)}).catch(function(){})},handleView:function(e){this.$emit("view",e)}}}},256:function(e,a,t){t(363);var s=t(0)(t(181),t(288),null,null);e.exports=s.exports},288:function(e,a,t){e.exports={render:function(){var e=this,a=e.$createElement,s=e._self._c||a;return s("div",{directives:[{name:"show",rawName:"v-show",value:!e.isLoading,expression:"!isLoading"}],staticClass:"workorder-page"},[s("ma-head",{attrs:{home:!e.isOperator},on:{"home-click":e.handleGoHome}},[e._v("工单详情")]),e._v(" "),s("div",{staticClass:"workorder-second-page",class:[{pb10:4==e.state}]},[s("ul",{staticClass:"member-info"},[s("li",{staticClass:"order-state-label"},[s("zs-icon",{attrs:{icon:["","serving","","nopay","finish"][e.state],size:"40","icon-dis":"15",text:["","未完工","","待付款","已完成"][e.state]}})],1),e._v(" "),s("li",{staticClass:"member-info-car"},[s("span",{staticClass:"title"},[e._v("服务车辆")]),e._v(" "),s("div",{staticClass:"info info-car-number",class:[{"is-member":e.memberRecordId}]},[s("span",{staticClass:"number"},[e._v(e._s(e.finalData.carNumber))]),e._v(" "),s("div",{staticClass:"lvlname-wrap"},[e.finalData.carNumber?s("p",{staticClass:"lvlname"},[e._v(e._s(e.memberLvl))]):e._e()])])]),e._v(" "),!e.isOperator&&e.memberRecordId?s("li",{staticClass:"arrow-right check-member",on:{click:e.handleToMemberDetail}},[e._v("\n                查看消费记录\n            ")]):e._e(),e._v(" "),s("li",[s("span",{staticClass:"title"},[e._v("客户姓名")]),e._v(" "),s("p",{staticClass:"info"},[s("span",[e._v(e._s(e.finalData.user.realName||"----"))]),e.remarks?s("span",{staticClass:"user-remarks"},[e._v("("+e._s(e.remarks)+")")]):e._e()])]),e._v(" "),s("li",[s("span",{staticClass:"title"},[e._v("手机号")]),e._v(" "),s("p",{staticClass:"info"},[e._v(e._s(e.finalData.mobile||"----"))])])]),e._v(" "),s("div",{staticClass:"item-info"},[s("p",{staticClass:"title"},[e._v("服务项目")]),e._v(" "),s("ul",{staticClass:"item-list"},e._l(e.itemSelectDatas,function(a,t){return s("li",{key:a.id},[s("div",{staticClass:"item-p-1"},[s("span",{staticClass:"item-name"},[e._v(e._s(a.serviceItemName)),s("i",[e._v("X"+e._s(a.itemsTimes))])]),e._v(" "),s("span",{staticClass:"item-operator"},[e._v("￥"+e._s(parseFloat(a.salePrice).toFixed(2)))]),e._v(" "),!e.isOperator&&3==e.state&&a._hasCard?s("zs-switch",{staticClass:"item-switch",on:{change:function(a){e.handleToggleUseCard(t)}},model:{value:a._useCard,callback:function(e){a._useCard=e},expression:"item._useCard"}}):e._e(),e._v(" "),!e.isOperator&&(1==e.state&&a._useCard||4==e.state&&a.usePackageTimes>0)?s("p",{staticClass:"use-card-label1"},[e._v("用卡")]):e._e()],1),e._v(" "),a.workerName||!e.isOperator&&a._hasCard?s("div",{staticClass:"item-p-2"},[s("span",{staticClass:"item-price"},[e._v(e._s(a.workerName))]),e._v(" "),!e.isOperator&&a._hasCard?s("div",{staticClass:"use-card-info"},[3==e.state?s("p",{staticClass:"use-card-label",class:[{"use-card":a._useCard}]},[e._v("\n                                "+e._s((a._useCard?"用卡":"不用卡")+"(剩余"+e.packageRemain[a.serviceItemId]+"次)"))]):e._e(),e._v(" "),3!=e.state&&4!=e.state?s("p",{staticClass:"card-remain"},[e._v("剩余"+e._s(e.packageRemain[a.serviceItemId])+"次")]):e._e()]):e._e()]):e._e(),e._v(" "),!e.isOperator&&(e.couponsData.length>0||a.couponDeduct>0)?s("div",{staticClass:"item-p-3",class:[{"arrow-right":3==e.state}],on:{click:function(s){e.handlePopCoupon(a.itemType,t)}}},[s("span",{staticClass:"sub-title"},[e._v("卡券抵扣"),a.couponName?s("i",[e._v("("+e._s(a.couponName)+")")]):e._e()]),e._v(" "),s("span",{staticClass:"discount-price",class:[{gray:!a.couponName}]},[e._v(e._s(e.handleCouponShow(a,t)))])]):e._e(),e._v(" "),e.handleDiscountShow(a)?s("div",{staticClass:"item-p-4"},[s("span",{staticClass:"sub-title"},[e._v("会员扣减")]),e._v(" "),s("span",{staticClass:"discount-price"},[e._v("-￥"+e._s(3!=e.state?a.discountPrice:e.handleCalcDiscountPrice(a,t)))])]):e._e(),e._v(" "),e.isOperator?e._e():s("div",{staticClass:"item-p-5"},[s("span",{staticClass:"sub-title"},[e._v("总金额")]),e._v(" "),s("span",{staticClass:"discount-price"},[e._v("￥"+e._s(3!=e.state?a.totalPrice:e.handleCalcItemTotalPrice(a,t)))])])])})),e._v(" "),s("p",{staticClass:"item-total-price"},[!e.isOperator&&parseFloat(e.packageDeduct)>0?[s("span",[e._v("套餐抵扣")]),s("i",[e._v("￥"+e._s(e.packageDeduct))])]:e._e(),e._v(" "),s("span",[e._v("项目费用")]),s("i",[e._v("￥"+e._s(e.itemCalcTotalPrice))])],2)]),e._v(" "),e.isOperator?e._e():s("ul",{staticClass:"deduct-info"},[3==e.state||e.finalData.materialCost>0?s("li",[s("zs-icon",{staticClass:"label-icon",attrs:{icon:"附",size:"18","icon-dis":"8","icon-background":"#e75845",text:"附加费用"}}),e._v(" "),3==e.state?s("zs-input",{attrs:{type:"number"},model:{value:e.finalData.materialCost,callback:function(a){e.finalData.materialCost=a},expression:"finalData.materialCost"}}):s("span",{staticClass:"deduct-text"},[e._v("￥ "+e._s(e.finalData.materialCost))])],1):e._e(),e._v(" "),3==e.state||e.finalData.businessDeduct>0?s("li",[s("zs-icon",{staticClass:"label-icon",attrs:{icon:"减",size:"18","icon-dis":"8","icon-background":"#2cc068",text:"商家扣减"}}),e._v(" "),3==e.state?[e._v("\n                    -\n                    "),s("zs-input",{attrs:{type:"number"},model:{value:e.finalData.businessDeduct,callback:function(a){e.finalData.businessDeduct=a},expression:"finalData.businessDeduct"}})]:s("span",{staticClass:"deduct-text"},[e._v("- ￥ "+e._s(e.finalData.businessDeduct))])],2):e._e(),e._v(" "),e.memberBalanceDeduct>0?s("li",{staticClass:"member-balance"},[s("div",{staticClass:"p-1",class:[{"state-other":3!=e.state}]},[s("zs-icon",{staticClass:"label-icon",attrs:{icon:"会",size:"18","icon-dis":"8","icon-background":"#4b8dde",text:"会员余额支付"}}),e._v("\n                    -\n                    "),3==e.state?[e._v("\n                        "+e._s(e.useBalance?Math.min(parseFloat(e.memberBalanceDeduct),parseFloat(e.totalPriceBeforeBalance||0)).toFixed(2):0)+"\n                        "),s("zs-switch",{model:{value:e.useBalance,callback:function(a){e.useBalance=a},expression:"useBalance"}})]:[e._v("\n                        ￥"+e._s(parseFloat(e.memberBalanceDeduct).toFixed(2))+"\n                    ")]],2),e._v(" "),4!=e.state?s("p",{staticClass:"p-2"},[e._v("(余额："+e._s(parseFloat(e.memberBalanceDeduct).toFixed(2))+")")]):e._e()]):e._e(),e._v(" "),4==this.state&&e._discountDeduct>0?s("li",[s("zs-icon",{staticClass:"label-icon",attrs:{icon:"折",size:"18","icon-dis":"8","icon-background":"#4b8dde",text:"会员折扣("+e._discountNum+"折)"}}),e._v(" "),s("span",{staticClass:"deduct-text"},[e._v("- ￥ "+e._s(e._discountDeduct))])],1):e._e(),e._v(" "),4==e.state&&e._couponDeduct>0?e._l(e.tempCouponShow,function(a,t){return a.couponDeduct>0?s("li",{key:t,staticClass:"coupon-wrap"},[s("zs-icon",{staticClass:"label-icon",attrs:{icon:"券",size:"18","icon-dis":"8","icon-background":"#ff8023",text:a.couponName||"优惠券"}}),e._v(" "),s("span",{staticClass:"deduct-text"},[e._v("- ￥ "+e._s(a.couponDeduct))])],1):e._e()}):e._e(),e._v(" "),e.isOperator?e._e():s("li",{staticClass:"final-total-price"},[e._v("\n                "+e._s(4==e.state?["","现金收款","微信收款"][e.finalData.payType]:"应收款")+"\n                "),s("i",[e._v("￥ "+e._s(4==e.state||1==e.state?e._totalPrice:e.realTotalPrice))])])],2),e._v(" "),e.isOperator||3!=e.state&&!e.finalData.comment?e._e():s("div",{staticClass:"order-comment",class:[{"is-disabled":3!=e.state}]},[s("span",{staticClass:"label-icon"},[e._v("备注")]),e._v(" "),3==e.state?s("zs-input",{attrs:{icon:"circle-cross",placeholder:"请输入工单备注"},model:{value:e.finalData.comment,callback:function(a){e.finalData.comment=a},expression:"finalData.comment"}}):s("span",[e._v(e._s(e.finalData.comment))])],1),e._v(" "),e.isOperator?e._e():s("div",{staticClass:"order-images"},[s("p",{staticClass:"title"},[e._v("施工图片"),4!=e.state?s("i",[e._v("(最多可上传6张照片)")]):e._e()]),e._v(" "),s("div",{staticClass:"up-load-box"},[4!=e.state?s("upload",{attrs:{images:e.images},on:{change:e.selectImageLink,delete:e.deleteWorkOrderImg,view:e.handleViewPic}}):[e.images.length>0?s("ul",{staticClass:"image-show-wrap clr"},e._l(e.images,function(a,t){return s("li",{key:a.id,staticClass:"image-show-item",on:{click:function(a){e.handleViewPic(t)}}},[s("div",{staticClass:"image-show",style:{backgroundImage:"url("+a.showImg+")"}})])})):s("p",{staticClass:"list-empty"},[e._v("未上传图片！")])]],2)]),e._v(" "),s("ul",{staticClass:"order-times"},[s("li",[s("span",[e._v("工单编号")]),e._v(" "+e._s(e.orderNo))]),e._v(" "),s("li",[s("span",[e._v("新建时间")]),e._v(" "+e._s(e.createTime))]),e._v(" "),3==e.state||4==e.state&&e.isOperator?s("li",[s("span",[e._v("完工时间")]),e._v(" "+e._s(e.commitTime))]):e._e(),e._v(" "),4!=e.state||e.isOperator?e._e():s("li",[s("span",[e._v("结算时间")]),e._v(" "+e._s(e.payTime))])]),e._v(" "),e.isOperator||1!=e.state?e._e():s("ul",{staticClass:"workorder-page-footer"},[s("li",{staticClass:"left bg-white",on:{click:e.handleDeleteOrderClick}},[e._v("取消工单\n            ")]),e._v(" "),s("li",{staticClass:"right",on:{click:e.updateWorkOrderForFinish}},[e._v("施工完成\n            ")])]),e._v(" "),e.isOperator||3!=e.state?e._e():s("ul",{staticClass:"workorder-page-footer no-pay-control"},[s("li",{staticClass:"left bg-white",on:{click:e.handleDeleteOrderClick}},[e._v("取消工单\n            ")]),e._v(" "),s("li",{staticClass:"left",class:[{"is-disabled":!e.canSubmit}],on:{click:e.handleCashPay}},[e._v("现金收款\n            ")]),e._v(" "),s("li",{staticClass:"right",class:[{"is-disabled":!e.canSubmit||0===parseFloat(e.realTotalPrice)}],on:{click:e.handleWechatPay}},[e._v("微信收款\n            ")])])]),e._v(" "),s("zs-dialog",{staticClass:"settle-pop",attrs:{"show-close":""},model:{value:e.payCashPopVisible,callback:function(a){e.payCashPopVisible=a},expression:"payCashPopVisible"}},[s("p",{slot:"title"},[s("zs-icon",{attrs:{icon:"nopay",size:"24","icon-dis":"8",text:"现金收款"}})],1),e._v(" "),s("p",{staticClass:"pay-pop-p1"},[e._v("￥ "+e._s(e.realTotalPrice))]),e._v(" "),s("p",{staticClass:"pay-pop-p2"},[e._v("是否收到现金或刷卡支付?")]),e._v(" "),s("ul",{staticClass:"pay-pop-control",slot:"footer"},[s("li",[s("zs-button",{on:{click:function(a){e.payCashPopVisible=!1}}},[e._v("取消")])],1),e._v(" "),s("li",[s("zs-button",{on:{click:e.updateWorkOrderForSettle}},[e._v("确定收款")])],1)])]),e._v(" "),s("zs-dialog",{staticClass:"settle-pop wechat-pay-pop",model:{value:e.payWechatPopVisible,callback:function(a){e.payWechatPopVisible=a},expression:"payWechatPopVisible"}},[s("p",{slot:"title"},[s("zs-icon",{attrs:{icon:"wechat-pay",size:"24","icon-dis":"8",text:"微信扫码付款"}})],1),e._v(" "),s("p",{ref:"barCodeWrap",staticClass:"pay-pop-barcode"},[s("img",{staticClass:"pay-pop-loading",attrs:{src:t(37)}})]),e._v(" "),s("p",{staticClass:"pay-pop-p1"},[e._v("￥ "+e._s(e.realTotalPrice))])]),e._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:e.couponSelectVisible,expression:"couponSelectVisible"}],staticClass:"coupon-modal",on:{click:function(a){e.couponSelectVisible=!1}}}),e._v(" "),s("div",{staticClass:"coupon-select-wrap",class:[{"coupon-show":e.couponSelectVisible}]},[s("ul",{staticClass:"coupon-list"},e._l(e.couponsItemData,function(a,t){return s("li",{key:a.id,staticClass:"coupons-item",on:{click:function(t){e.handleSelectCoupon(a.takeUuid)}}},[s("div",{staticClass:"coupons-price"},[s("span",{staticClass:"coupons-price-label"},[e._v("￥")]),e._v(e._s(a.coupon.cpMoney)+"\n                    "),s("p",{staticClass:"coupons-price-type"},[e._v("现金券")])]),e._v(" "),s("div",{staticClass:"coupons-detail"},[s("p",{staticClass:"coupons-name"},[e._v(e._s(a.coupon.cpName))]),e._v(" "),s("p",{staticClass:"coupons-date"},[e._v("\n                        "+e._s(a.useBeginDay.slice(0,10)+"到"+a.useEndDay.slice(0,10)))])]),e._v(" "),s("zs-icon",{staticClass:"coupons-checked",attrs:{icon:a.couponKey===e.itemSelectDatas[e.currItemIndex].couponKey?"check":"uncheck",size:"20"}})],1)}))]),e._v(" "),s("swiper",{attrs:{images:e.images,index:e.imageIndex},model:{value:e.imageViewVisible,callback:function(a){e.imageViewVisible=a},expression:"imageViewVisible"}})],1)},staticRenderFns:[]}},30:function(e,a,t){t(34);var s=t(0)(t(25),t(31),null,null);e.exports=s.exports},31:function(e,a){e.exports={render:function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("ul",{staticClass:"upload-photo__wrap clr"},[e._l(e.images,function(a,s){return t("li",{on:{click:function(a){e.handleView(s)}}},[t("div",{staticClass:"image-label",style:{backgroundImage:"url('"+a.showImg+"')"}},[t("span",{staticClass:"image-del-icon",on:{click:function(t){t.stopPropagation(),e.handleDelImg(s,a.id)}}})])])}),e._v(" "),e.isFull?e._e():t("li",{staticClass:"upload-photo__btn"},[t("label",{staticClass:"image-label add-label",attrs:{for:"imgInput"}},[t("input",{staticClass:"imgInput",attrs:{id:"imgInput",type:"file",accept:"image/*"},on:{change:e.handlePushImg}})])])],2)},staticRenderFns:[]}},34:function(e,a){},363:function(e,a){},486:function(e,a,t){e.exports=t(83)},83:function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s=t(1),i=t(256),o=t.n(i),c=t(5),n=(t.n(c),t(3),t(4));s.a.use(n.a),new s.a({el:"#app",render:function(e){return e(o.a)}})}},[486]);