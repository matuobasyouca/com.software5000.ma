webpackJsonp([21],{176:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=a(2),i=a(10),o=a(6),c=a.n(o),n=a(8),r=a.n(n);t.default={components:{maHead:c.a,carNumber:r.a},data:function(){return{isLoading:!0,isEdit:!1,nextPageVisible:!1,isOperator:"",operatorId:"",operatorName:"",operatorItemType:"",currCarArea:"",currCarLetter:"",currCarNum:"",memberRecordId:"",memberLvl:"",packageRemain:{},remarks:"",discount:10,itemTypes:[],itemType:"",itemDatas:[],itemSelectDatas:[],totalSelectItemId:[],finalData:{id:"",totalPrice:"",carNumber:"",workOrderDetails:[],userId:"",user:{realName:""},state:1,mobile:"",businessDeduct:"",materialCost:"",payType:"",comment:""},couponSelectVisible:!1,couponsData:[],couponsItemData:[],couponKey:1,currItemIndex:"",currItemType:""}},computed:{itemTotalPrice:function(){var e=0;return this.itemSelectDatas.forEach(function(t){e+=t.itemsTimes*t.salePrice}),parseFloat(a.i(s.c)(e)?0:e).toFixed(2)},itemCalcTotalPrice:function(){var e=0;return this.itemSelectDatas.forEach(function(t){e+=parseFloat(t.totalPrice)}),parseFloat(e).toFixed(2)},priceBeforeBusinessDeduct:function(){return parseFloat(this.itemCalcTotalPrice)+parseFloat(this.finalData.materialCost||0)},totalPrice:function(){var e=(this.priceBeforeBusinessDeduct-parseFloat(this.finalData.businessDeduct||0)).toFixed(2);return a.i(s.c)(e)?"0":e>0?e:"0"}},methods:{handleToMemberDetail:function(){var e=this;a.i(s.c)(this.memberRecordId)||this.isOperator||(window.localStorage.wo=JSON.stringify(this.$data),setTimeout(function(){a.i(s.d)("/wap/merchant/home/member/detail.html",{id:e.memberRecordId})},100))},handleGoHome:function(){a.i(s.d)("/wap/merchant/home/manage/index.html")},selectDefaultCarArea:function(e,t){var i=this;this.$ajax(this.$joggle.business.selectBusinessInfo,{},t,function(t){if("ZS011000"===t.code){if(!a.i(s.c)(t.data)){var o=t.data.defaultCar;i.currCarArea=o?o[0]:"闽",i.currCarLetter=o?o[1]:"D"}}else i.$message({type:"error",message:t.msg,duration:1200});e&&e()},function(){e&&e()})},handleGetCarNumber:function(e){if(this.finalData.carNumber=e,!(e.length<7))if(a.i(s.h)(e))if(a.i(s.c)(window.localStorage.wo)){var t=this.$loading();this.selectWorkOrder(function(){t.close()},t)}else window.localStorage.wo="";else this.$message({type:"error",message:"车牌号码有误!"})},selectWorkOrder:function(e,t,i){var o=this;this.$ajax(this.$joggle.merchant.workorder[a.i(s.c)(i)?"selectWorkOrderByCarNumber":"selectWorkOrderById"],a.i(s.c)(i)?{carNumber:this.finalData.carNumber}:{orderId:i},t,function(t,c){if("ZS011000"===t.code){var n=t.data.workOrder,r=t.data.user,l=a.i(s.c)(r)?"":r.mobile;if(o.totalSelectItemId=o.isOperator?t.data.serviceIdList||[]:[],o.packageRemain=o.packageMerge(a.i(s.c)(r)?[]:r.packageList),!a.i(s.c)(i)){if(a.i(s.c)(n))return void o.$message({type:"error",message:"工单不存在！"});o.finalData.carNumber=a.i(s.c)(n)?"":n.carNumber}o.finalData.id=a.i(s.c)(n)?"":n.id,o.finalData.mobile=a.i(s.c)(n)?l:n.mobile||"",o.finalData.businessDeduct=a.i(s.c)(n)?"":n.businessDeduct||"",o.finalData.materialCost=a.i(s.c)(n)?"":n.materialCost||"",o.finalData.comment=a.i(s.c)(n)?"":n.comment||"",o.finalData.user.realName=a.i(s.c)(r)?"":r.realName,o.finalData.userId=a.i(s.c)(r)?"":r.userId||"",o.memberRecordId=a.i(s.c)(r)?"":r.recordId||"",o.remarks=a.i(s.c)(r)?"":r.remarks||"",o.memberLvl=a.i(s.c)(o.memberRecordId)?"非会员":r.lvlName||"普通会员",o.discount=a.i(s.c)(r)?10:r.discount||"0"==r.discount?r.discount:10,o.selectCouponsByCarNumber(function(){var t=a.i(s.c)(n)?[]:n.workOrderDetails||[];o.itemDatas.forEach(function(e){for(var a=!0,s=0;s<t.length;s++)if(t[s].serviceItemId==e.id){o.$set(e,"itemsTimes",t[s].itemsTimes),a=!1;break}a&&o.$set(e,"itemsTimes",0)}),t.forEach(function(e){e._hasCard=e._useCard=!1,(a.i(s.c)(e.itemType)||a.i(s.c)(e.serviceItemId))&&(e.itemType=-1),e._autoCoupon=!1,e.discountNumber=o.discount;for(var t in o.packageRemain)if(o.packageRemain[t]>0&&t==e.serviceItemId){e._hasCard=!0,e._useCard=parseInt(e.usePackageTimes||0)>0,e.usePackageTimes=Math.min(e.itemsTimes,o.packageRemain[t]);break}o.$set(e,"couponKey",o.couponKey);var i=!1;a.i(s.c)(e.couponUuid)||(o.couponsData.forEach(function(t){e.couponUuid===t.takeUuid&&(o.$set(t,"couponKey",o.couponKey),i=!0)}),i||(o.$set(e,"couponUuid",""),o.$set(e,"couponName",""),o.$set(e,"couponDeduct",""))),o.couponKey++}),o.itemSelectDatas=t,e&&e()},c)}else c.close(),o.$message({type:"error",message:t.msg,duraton:1200})},function(){o.finalData.id="",o.finalData.totalPrice=0,o.finalData.workOrderDetails=[],o.finalData.userId="",o.finalData.user.realName="",o.finalData.mobile="",o.finalData.businessDeduct="",o.finalData.materialCost="",o.finalData.comment="",o.itemSelectDatas=[],o.itemDatas.forEach(function(e){o.$set(e,"itemsTimes",0)}),o.memberLvl="",o.packageRemain={}})},packageMerge:function(e){var t={1:0};if(e.length>0)for(var a=0;a<e.length;a++)if(e[a].memberItemUseRecords.length>0)for(var s=e[a].memberItemUseRecords,i=0;i<s.length;i++){var o=!0;for(var c in t)if(parseInt(c)===parseInt(s[i].serviceItem.id)){t[c]=parseInt(t[c])+parseInt(s[i].remainTimes),o=!1;break}o&&(t[s[i].serviceItem.id]=s[i].remainTimes)}return t},selectItemType:function(e,t){var a=this;this.$ajax(this.$joggle.mzscp.selectConstantTypes,{codeTypes:"ServiceItemType"},t,function(t){if("ZS011000"===t.code){var s=t.data.ServiceItemType;s.unshift(["","全部"]),a.itemTypes=s}else a.$message({type:"error",message:t.msg,duration:1200});e&&e()})},handleSelectItemType:function(e){this.itemType=e;var t=this.$loading();this.selectServiceItem(function(){t.close()},t)},selectServiceItem:function(e,t){var a=this;this.$ajax(this.$joggle.merchant.workorder.selectServiceItem,{itemType:this.itemType},t,function(t){if("ZS011000"===t.code){var s=t.data;s.forEach(function(e){for(var t=!0,s=0;s<a.itemSelectDatas.length;s++)if(a.itemSelectDatas[s].serviceItemId==e.id){a.$set(e,"itemsTimes",a.itemSelectDatas[s].itemsTimes),t=!1;break}t&&a.$set(e,"itemsTimes",0)}),a.itemDatas=s}else a.$message({type:"error",message:t.msg});e&&e()})},itemTimesChange:function(e,t){for(var i=this,o=!0,c=Math.min(t,this.packageRemain[e.id]||0),n=0;n<this.itemSelectDatas.length;n++){var r=i.itemSelectDatas[n];if(r.serviceItemId===e.id){if(0===t){for(var l=0;l<this.couponsData.length;l++)if(i.couponsData[l].couponKey===i.itemSelectDatas[n].couponKey){i.$set(i.couponsData[l],"couponKey","");break}i.$delete(i.itemSelectDatas,n)}else{r.itemsTimes=t,r.usePackageTimes=c;var u=i.selectMaxCoupon(r,r.couponKey);if(!a.i(s.c)(u.takeUuid)&&r._autoCoupon)i.$set(r,"couponUuid",u.takeUuid),i.$set(r,"couponName",u.coupon.cpName),i.$set(r,"couponDeduct",u.coupon.cpMoney),i.$set(r,"_useCard",!1);else{i.$set(r,"couponUuid",""),i.$set(r,"couponName",""),i.$set(r,"couponDeduct",0);for(var m=0;m<this.couponsData.length;m++)if(i.couponsData[m].couponKey===r.couponKey){i.$set(i.couponsData[m],"couponKey","");break}}}o=!1;break}}if(o){var p=this.packageRemain[e.id]&&this.packageRemain[e.id]>0,d=this.couponKey;e._autoCoupon=!0;var h=this.selectMaxCoupon(e,d),f={id:"",serviceItemId:e.id,serviceItemName:e.itemName,itemsTimes:t,usePackageTimes:c,salePrice:e.salePrice,workerId:this.isOperator?this.operatorId:"",salerId:this.isOperator?this.operatorId:"",workerName:this.isOperator?this.operatorName:"",salerName:this.isOperator?this.operatorName:"",itemType:e.itemType,discountPrice:0,discountNumber:this.discount,couponKey:d,couponUuid:h.takeUuid||"",couponName:h.coupon?h.coupon.cpName||"":"",couponDeduct:h.coupon?h.coupon.cpMoney||0:0,totalPrice:0,_autoCoupon:!this.isOperator,_hasCard:p,_useCard:p&&a.i(s.c)(h.takeUuid)};this.itemSelectDatas.push(f),this.couponKey++}},itemTimesNotice:function(e){this.totalSelectItemId.indexOf(e.id)>-1&&this.$message({type:"error",message:"已有员工创建该项目"})},handleNext:function(){0!==this.itemSelectDatas.length&&(this.isOperator?this.finalSubmit():a.i(s.h)(this.finalData.carNumber)?this.nextPageVisible=!0:this.$message({type:"error",message:"车牌号码有误！"}))},toggleUseCard:function(e){var t=this,a=this.itemSelectDatas[e];if(this.$set(a,"_autoCoupon",!1),a._useCard){this.$set(a,"couponUuid",""),this.$set(a,"couponName",""),this.$set(a,"couponDeduct",0);for(var s=0;s<this.couponsData.length;s++)if(t.couponsData[s].couponKey===a.couponKey){t.$set(t.couponsData[s],"couponKey","");break}}},calcDiscountPrice:function(e,t){var a=parseFloat((1-e.discountNumber/10)*((e.itemsTimes-(e._useCard?e.usePackageTimes:0))*e.salePrice-(e.couponDeduct||0))).toFixed(2);return this.$set(this.itemSelectDatas[t],"discountPrice",parseFloat(a)),a},calcItemTotalPrice:function(e,t){var a=parseFloat((e.itemsTimes-(e._useCard?e.usePackageTimes:0))*e.salePrice-(e.couponDeduct||0)-e.discountPrice).toFixed(2);return this.$set(this.itemSelectDatas[t],"totalPrice",parseFloat(a)),a},selectCouponsByCarNumber:function(e,t){var a=this;if(this.isOperator)return void(e&&e());this.$ajax(this.$joggle.merchant.workorder.selectCouponsByCarNumber,{carNumber:this.finalData.carNumber},t,function(t){"ZS011000"===t.code?a.couponsData=t.data:a.$message({type:"error",message:t.msg,duration:1200}),e&&e()})},selectMaxCoupon:function(e,t){var i=e.itemType,o=e.salePrice*(e.itemsTimes||1),c={coupon:{cpMoney:0}},n=-1;return this.couponsData.forEach(function(t,r){e._autoCoupon&&(a.i(s.c)(t.couponKey)||t.couponKey===e.couponKey)&&(!t.coupon.itemType||i==t.coupon.itemType)&&t.coupon.cpMoney-o<=0&&t.coupon.cpMoney-c.coupon.cpMoney>=0&&(c=t,n=r)}),n>-1?(this.$set(this.couponsData[n],"couponKey",t),this.couponsData[n]):{}},handlePopCoupon:function(e,t){var a=this;if(this.itemSelectDatas[t]._useCard)return void this.$message({type:"warning",message:"已选择使用套餐，不能使用优惠券！"});this.currItemIndex=t,this.currItemType=e||"-1";var s=[];this.couponsData.forEach(function(e,t){a.isCouponListShow(e)&&(e._index=t,s.push(e))}),this.couponsItemData=s,this.couponsItemData.length>0&&(this.couponSelectVisible=!0)},isCouponListShow:function(e,t){t=a.i(s.c)(t)?this.currItemIndex:t;var i=this.itemSelectDatas[t],o=i.itemType;return!a.i(s.c)(i)&&((!e.couponKey||e.couponKey==i.couponKey)&&(!e.coupon.itemType||e.coupon.itemType==o)&&e.coupon.cpMoney-i.itemsTimes*i.salePrice<=0)},handleSelectCoupon:function(e){var t=this,a=this.itemSelectDatas[this.currItemIndex];this.$set(a,"_autoCoupon",!1);for(var s=0;s<this.couponsData.length;s++)if(t.couponsData[s].takeUuid===e){t.couponsData[s].couponKey===a.couponKey?(t.$set(t.couponsData[s],"couponKey",""),t.$set(a,"couponUuid",""),t.$set(a,"couponName",""),t.$set(a,"couponDeduct",0)):(t.couponsItemData.forEach(function(e){t.$set(e,"couponKey",""),t.$set(t.couponsData[e._index],"couponKey","")}),t.$set(t.couponsData[s],"couponKey",a.couponKey),t.$set(a,"couponUuid",t.couponsData[s].takeUuid),t.$set(a,"couponName",t.couponsData[s].coupon.cpName),t.$set(a,"couponDeduct",t.couponsData[s].coupon.cpMoney));break}this.couponSelectVisible=!1},couponShow:function(e,t){var a=this,s=[];return this.couponsData.forEach(function(e){a.isCouponListShow(e,t)&&s.push(e)}),0===s.length?"暂无可用卡券":e.couponName?"-￥"+e.couponDeduct:"请选择卡券"},finalSubmit:function(){var e=this;this.itemSelectDatas.forEach(function(e){!e._useCard&&(e.usePackageTimes=0)}),this.finalData.workOrderDetails=this.itemSelectDatas,this.finalData.totalPrice=parseFloat(this.totalPrice),this.finalData.businessDeduct=Math.min(this.priceBeforeBusinessDeduct,parseFloat(this.finalData.businessDeduct||0)).toFixed(2),this.finalData.materialCost=parseFloat(this.finalData.materialCost||0).toFixed(2),a.i(s.h)(this.finalData.carNumber)?a.i(s.c)(this.finalData.mobile)||a.i(s.i)(this.finalData.mobile)?this.$ajax(this.$joggle.merchant.workorder.updateWorkOrder,this.finalData,!0,function(t,i){i.close(),"ZS011000"===t.code?(e.$message({type:"success",message:t.msg}),setTimeout(function(){a.i(s.d)("/wap/merchant/home/workorder/list.html"+(e.isOperator?"#eyJ0YWIiOjF9":"#eyJ0YWIiOjJ9"))},1200)):e.$message({type:"error",message:t.msg})}):this.$message({type:"error",message:"请输入正确的手机号码!"}):this.$message({type:"error",message:"车牌号码有误!"})}},mounted:function(){var e=this;if(window.localStorage.wo){for(var t in this.$data)e[t]=JSON.parse(window.localStorage.wo)[t];return this.currCarArea=this.finalData.carNumber[0],this.currCarLetter=this.finalData.carNumber[1],this.currCarNum=this.finalData.carNumber.slice(2),void(this.isLoading=!1)}a.i(i.a)(function(t){e.isOperator=!!t.mercharType&&t.mercharType.indexOf("operator")>-1,e.operatorId=t.id||"",e.operatorItemType=t.itemTypes||"1,2,3,4,5",e.operatorName=t.userName||""}),this.finalData.id=a.i(s.a)("id")||"",this.isEdit=!a.i(s.c)(this.finalData.id);var o=this.$loading(),c=new Promise(function(t){e.selectItemType(function(){t()},o)}),n=new Promise(function(t){e.selectServiceItem(function(){a.i(s.c)(e.finalData.id)?t():e.selectWorkOrder(function(){t()},o,e.finalData.id)},o)}),r=new Promise(function(t){e.isEdit?t():e.selectDefaultCarArea(function(){t()},o)});Promise.all([c,n,r]).then(function(){o.close(),e.isLoading=!1})}}},246:function(e,t,a){a(361);var s=a(0)(a(176),a(261),null,null);e.exports=s.exports},261:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{directives:[{name:"show",rawName:"v-show",value:!e.isLoading,expression:"!isLoading"}],staticClass:"workorder-page"},[a("ma-head",{attrs:{home:!e.isOperator},on:{"home-click":e.handleGoHome}},[e._v(e._s(e.isEdit?"修改工单":"开单"))]),e._v(" "),e.isEdit?a("div",{staticClass:"car-number-show"},[a("zs-icon",{attrs:{icon:"car",size:"20",text:"服务车辆"}}),e._v("\n        "+e._s(this.finalData.carNumber)+"\n    ")],1):a("car-number",{attrs:{"car-province":e.currCarArea,"car-letter":e.currCarLetter,"car-num":e.currCarNum,"photo-icon":""},on:{"get-car-number":e.handleGetCarNumber}}),e._v(" "),a("div",{staticClass:"item-select"},[a("ul",{staticClass:"menu"},e._l(e.itemTypes,function(t){return!e.isOperator||e.operatorItemType.indexOf(t[0])>-1?a("li",{key:t[0],class:[{curr:e.itemType===t[0]}],on:{click:function(a){e.handleSelectItemType(t[0])}}},[e._v(e._s(t[1])+"\n            ")]):e._e()})),e._v(" "),a("ul",{staticClass:"list"},e._l(e.itemDatas,function(t){return a("li",{key:t.id},[a("p",{staticClass:"item-name"},[e._v(e._s(t.itemName))]),e._v(" "),a("div",{staticClass:"item-detail"},[a("span",{staticClass:"item-price"},[e._v("￥"+e._s(t.salePrice))]),e._v(" "),a("zs-input-number",{class:[{"is-zero":0==t.itemsTimes}],attrs:{min:0,max:e.totalSelectItemId.indexOf(t.id)>-1?0:1e3,disabled:e.totalSelectItemId.indexOf(t.id)>-1},on:{click:function(a){e.itemTimesNotice(t)},change:function(a){e.itemTimesChange(t,a)}},model:{value:t.itemsTimes,callback:function(e){t.itemsTimes=e},expression:"item.itemsTimes"}})],1)])}))]),e._v(" "),a("div",{staticClass:"workorder-page-footer"},[e.itemSelectDatas.length>0?a("span",{staticClass:"total-price"},[e._v("合计"),a("i",[e._v("￥"+e._s(e.itemTotalPrice))])]):a("span",{staticClass:"notice"},[e._v("未选择服务项目")]),e._v(" "),a("p",{staticClass:"control",class:[{"is-disabled":0===e.itemSelectDatas.length}],on:{click:e.handleNext}},[e._v("\n            "+e._s(e.isOperator?"提交工单":"下一步"))])]),e._v(" "),a("zs-slide-page",{attrs:{title:"开单"},on:{"go-home":e.handleGoHome},model:{value:e.nextPageVisible,callback:function(t){e.nextPageVisible=t},expression:"nextPageVisible"}},[a("div",{staticClass:"workorder-second-page"},[a("ul",{staticClass:"member-info"},[a("li",{staticClass:"member-info-car"},[a("span",{staticClass:"title"},[e._v("服务车辆")]),e._v(" "),a("div",{staticClass:"info info-car-number",class:[{"is-member":e.memberRecordId}]},[a("span",{staticClass:"number"},[e._v(e._s(e.finalData.carNumber))]),e._v(" "),a("div",{staticClass:"lvlname-wrap"},[e.finalData.carNumber?a("p",{staticClass:"lvlname"},[e._v(e._s(e.memberLvl))]):e._e()])])]),e._v(" "),!e.isOperator&&e.memberRecordId?a("li",{staticClass:"arrow-right check-member",on:{click:e.handleToMemberDetail}},[e._v("查看消费记录\n                ")]):e._e(),e._v(" "),a("li",{staticClass:"user-realname"},[a("span",{staticClass:"title"},[e._v("客户姓名")]),e._v(" "),a("p",{staticClass:"info"},[a("zs-input",{attrs:{icon:"circle-cross",placeholder:"请输入客户姓名"},model:{value:e.finalData.user.realName,callback:function(t){e.finalData.user.realName=t},expression:"finalData.user.realName"}})],1),e._v(" "),e.remarks?a("span",{staticClass:"user-remarks"},[e._v("（ "+e._s(e.remarks)+"）")]):e._e()]),e._v(" "),a("li",[a("span",{staticClass:"title"},[e._v("手机号")]),e._v(" "),a("p",{staticClass:"info"},[a("zs-input",{attrs:{icon:"circle-cross",maxlength:11,placeholder:"请输入手机号"},model:{value:e.finalData.mobile,callback:function(t){e.finalData.mobile=t},expression:"finalData.mobile"}})],1)])]),e._v(" "),a("div",{staticClass:"item-info"},[a("p",{staticClass:"title"},[e._v("服务项目")]),e._v(" "),a("ul",{staticClass:"item-list"},e._l(e.itemSelectDatas,function(t,s){return a("li",{key:t.id},[a("div",{staticClass:"item-p-1"},[a("span",{staticClass:"item-name"},[e._v(e._s(t.serviceItemName)),a("i",[e._v("X"+e._s(t.itemsTimes))])]),e._v(" "),a("span",{staticClass:"item-operator"},[e._v("￥"+e._s(parseFloat(t.salePrice).toFixed(2)))]),e._v(" "),t._hasCard?a("zs-switch",{staticClass:"item-switch",on:{change:function(t){e.toggleUseCard(s)}},model:{value:t._useCard,callback:function(e){t._useCard=e},expression:"item._useCard"}}):e._e()],1),e._v(" "),t.workerName||t._useCard?a("div",{staticClass:"item-p-2"},[a("span",{staticClass:"item-price"},[e._v(e._s(t.workerName))]),e._v(" "),t._hasCard?a("div",{staticClass:"use-card-info"},[a("p",{staticClass:"use-card-label",class:[{"use-card":t._useCard}]},[e._v("\n                                    "+e._s((t._useCard?"用卡":"不用卡")+"(剩余"+e.packageRemain[t.serviceItemId]+"次)")+"\n                                ")])]):e._e()]):e._e(),e._v(" "),e.couponsData.length>0?a("div",{staticClass:"item-p-3 arrow-right",on:{click:function(a){e.handlePopCoupon(t.itemType,s)}}},[a("span",{staticClass:"sub-title"},[e._v("卡券抵扣"),t.couponName?a("i",[e._v("("+e._s(t.couponName)+")")]):e._e()]),e._v(" "),a("span",{staticClass:"discount-price",class:[{gray:!t.couponName}]},[e._v(e._s(e.couponShow(t,s)))])]):e._e(),e._v(" "),(t.discountNumber||"0"==t.discountNumber)&&t.discountNumber<10?a("div",{staticClass:"item-p-4"},[a("span",{staticClass:"sub-title"},[e._v("会员扣减")]),e._v(" "),a("span",{staticClass:"discount-price"},[e._v("-￥"+e._s(e.calcDiscountPrice(t,s)))])]):e._e(),e._v(" "),a("div",{staticClass:"item-p-5"},[a("span",{staticClass:"sub-title"},[e._v("总金额")]),e._v(" "),a("span",{staticClass:"discount-price"},[e._v("￥"+e._s(e.calcItemTotalPrice(t,s)))])])])})),e._v(" "),a("p",{staticClass:"item-total-price"},[e._v("项目费用"),a("i",[e._v("￥"+e._s(e.itemCalcTotalPrice))])])]),e._v(" "),a("ul",{staticClass:"deduct-info"},[a("li",[a("zs-icon",{staticClass:"label-icon",attrs:{icon:"附",size:"18","icon-dis":"8","icon-background":"#e75845",text:"附加费用"}}),e._v(" "),a("zs-input",{attrs:{icon:"circle-cross",type:"number",placeholder:"请输入附加费用"},model:{value:e.finalData.materialCost,callback:function(t){e.finalData.materialCost=t},expression:"finalData.materialCost"}})],1),e._v(" "),a("li",[a("zs-icon",{staticClass:"label-icon",attrs:{icon:"减",size:"18","icon-dis":"8","icon-background":"#2cc068",text:"商家扣减"}}),e._v(" "),a("zs-input",{attrs:{icon:"circle-cross",type:"number",placeholder:"请输入商家扣减"},model:{value:e.finalData.businessDeduct,callback:function(t){e.finalData.businessDeduct=t},expression:"finalData.businessDeduct"}})],1),e._v(" "),a("li",[a("span",{staticClass:"label-icon"},[e._v("备注")]),e._v(" "),a("zs-input",{attrs:{icon:"circle-cross",placeholder:"请输入工单备注"},model:{value:e.finalData.comment,callback:function(t){e.finalData.comment=t},expression:"finalData.comment"}})],1)]),e._v(" "),a("div",{staticClass:"workorder-page-footer"},[a("span",{staticClass:"total-price"},[e._v("合计"),a("i",[e._v("￥"+e._s(e.totalPrice))])]),e._v(" "),a("p",{staticClass:"control return-control",on:{click:function(t){e.nextPageVisible=!1}}},[e._v("上一步")]),e._v(" "),a("p",{staticClass:"control",on:{click:e.finalSubmit}},[e._v("保存工单")])])]),e._v(" "),a("div",{directives:[{name:"show",rawName:"v-show",value:e.couponSelectVisible,expression:"couponSelectVisible"}],staticClass:"coupon-modal",on:{click:function(t){e.couponSelectVisible=!1}}}),e._v(" "),a("div",{staticClass:"coupon-select-wrap",class:[{"coupon-show":e.couponSelectVisible}]},[a("ul",{staticClass:"coupon-list"},e._l(e.couponsItemData,function(t,s){return a("li",{key:t.id,staticClass:"coupons-item",on:{click:function(a){e.handleSelectCoupon(t.takeUuid)}}},[a("div",{staticClass:"coupons-price"},[a("span",{staticClass:"coupons-price-label"},[e._v("￥")]),e._v(e._s(t.coupon.cpMoney)+"\n                        "),a("p",{staticClass:"coupons-price-type"},[e._v("现金券")])]),e._v(" "),a("div",{staticClass:"coupons-detail"},[a("p",{staticClass:"coupons-name"},[e._v(e._s(t.coupon.cpName))]),e._v(" "),a("p",{staticClass:"coupons-date"},[e._v("\n                            "+e._s(t.useBeginDay.slice(0,10)+"到"+t.useEndDay.slice(0,10)))])]),e._v(" "),a("zs-icon",{staticClass:"coupons-checked",attrs:{icon:t.couponKey===e.itemSelectDatas[e.currItemIndex].couponKey?"check":"uncheck",size:"20"}})],1)}))])])],1)},staticRenderFns:[]}},361:function(e,t){},463:function(e,t,a){e.exports=a(82)},82:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=a(1),i=a(246),o=a.n(i),c=a(5),n=(a.n(c),a(3),a(4));s.a.use(n.a),new s.a({el:"#app",render:function(e){return e(o.a)}})}},[463]);