webpackJsonp([14],{194:function(t,r,s){"use strict";Object.defineProperty(r,"__esModule",{value:!0});var c=s(8),o=s(12),i=s(11),e=s.n(i);r.default={components:{maFoot:e.a},methods:{login:function(t){var r=this,i=["jiye","18950386831","op2"],e=["123456","c1","a123456"],n={userId:i[t],password:c.a.encode(e[t]),userType:"merchant"};this.$ajax(this.$joggle.merchant.open.login,n,!0,function(t,c){c.close(),"ZS001000"===t.code?(s.i(o.b)(),r.$message({type:"success",message:"丫的登陆成功了啊"})):r.$message({type:"error",message:"傻X，失败了吧"})})}},created:function(){}}},269:function(t,r,s){s(411);var c=s(0)(s(194),s(344),null,null);t.exports=c.exports},344:function(t,r){t.exports={render:function(){var t=this,r=t.$createElement,s=t._self._c||r;return s("div",{staticClass:"test"},[s("div",{staticClass:"block"},[s("p",{staticClass:"block-title"},[t._v("icon")]),t._v(" "),s("div",{staticClass:"block-bd"},[s("p",{staticClass:"sub-title"},[t._v("特殊的文字icon")]),t._v(" "),s("zs-icon",{attrs:{icon:"first-letter",size:"18",text:"首字"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"first-letter","icon-background":"#000",size:"18",text:"首字设置颜色"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"字",size:"18",text:"自定义文字"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"字",icon2:"二",size:"18",text:"自定义2个文字"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"字",icon2:"二","icon-background":"red","icon2-background":"green",size:"18",text:"自定义2个文字设置颜色"}}),t._v(" "),s("br"),t._v(" "),s("p",{staticClass:"sub-title"},[t._v("所有icon")]),t._v(" "),s("zs-icon",{attrs:{icon:"arrow-top",size:"14",text:"arrow-top"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"arrow-right",size:"14",text:"arrow-right"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"arrow-bottom",size:"14",text:"arrow-bottom"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"arrow-left",size:"14",text:"arrow-left"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"dot",text:"arrow-dot"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"dot-large",text:"arrow-dot-large"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"dot-gray",text:"arrow-dot-gray"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"dot-large-gray",text:"arrow-dot-large-gray"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"ten-cross",text:"ten-cross"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"cross",text:"cross"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"add",text:"add"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"circle-cross",text:"circle-cross"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"circle-error",text:"circle-error"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"tick",text:"tick"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"circle-success",text:"circle-success"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"warning",text:"warning"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"home",text:"home"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"camera",text:"camera"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"circle-error-w",text:"circle-error-w"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"circle-success-w",text:"circle-success-w"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"user",text:"user"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"mobile",text:"mobile"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"mobile2",text:"mobile2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"member",text:"member"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"no-member",text:"no-member"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"binding",text:"binding"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"merchant",text:"merchant"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"car",text:"car"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"wechat-pay",text:"wechat-pay"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"cash-pay",text:"cash-pay"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"nopay",text:"nopay",size:"40"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"serving",text:"serving",size:"40"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"finish",text:"finish",size:"40"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"uncheck",text:"uncheck"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"check",text:"check"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"delete",text:"delete"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"write",text:"write"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"delete2",text:"delete2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"delete3",text:"delete3"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"order",text:"order",size:"30"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"shouzhi",text:"shouzhi"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"cooper",text:"cooper"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"cooper_order",text:"cooper_order"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"dengji",text:"dengji"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"pack",text:"pack"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"shang1",text:"shang1"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"time2",text:"time2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"time4",text:"time4"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"car2",text:"car2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"wechat",text:"wechat"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"time",text:"time"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"shang",text:"shang"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"call",text:"call"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"location",text:"location"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"location2",text:"location2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"search",text:"search"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"search1",text:"search1"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"back",text:"back"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"shang2",text:"shang2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"pack_green",text:"pack_green"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"time3",text:"time3"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"check2",text:"check2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"no_data",text:"no_data"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"bell",text:"bell"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"cheng",text:"cheng"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"order-pay",text:"order-pay"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"quit",text:"quit"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"cash",text:"cash"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"cash-pay2",text:"cash-pay2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"check3",text:"check3"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"cross2",text:"cross2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"doubt",text:"doubt"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"user2",text:"user2"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"remark",text:"remark"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"follow",text:"follow"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"shang4",text:"shang4"}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"stay",text:"stay",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"guide-arrow",text:"guide-arrow",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"balance",text:"balance",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"recharge",text:"recharge",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"largess",text:"largess",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"shang3",text:"shang3",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"group-fail",text:"group-fail",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"group-grouping",text:"group-grouping",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"group-success",text:"group-success",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"expansion",text:"expansion",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"packup",text:"packup",size:40}}),t._v(" "),s("br"),t._v(" "),s("zs-icon",{attrs:{icon:"more-people",text:"more-people",size:40}}),t._v(" "),s("br")],1)]),t._v(" "),s("div",{staticClass:"block"},[s("p",{staticClass:"block-title"},[t._v("临时登陆")]),t._v(" "),s("zs-button",{staticClass:"login",attrs:{type:"danger"},on:{click:function(r){t.login(0)}}},[t._v("登陆")]),t._v(" "),s("br"),s("br"),t._v(" "),s("zs-button",{staticClass:"login",attrs:{type:"primary"},on:{click:function(r){t.login(1)}}},[t._v("操作员1登陆")]),t._v(" "),s("br"),s("br"),t._v(" "),s("zs-button",{staticClass:"login",attrs:{type:"warning"},on:{click:function(r){t.login(2)}}},[t._v("操作员2登陆")])],1),t._v(" "),s("div",{staticClass:"block"},[s("p",{staticClass:"block-title"},[t._v("页脚")]),t._v(" "),s("ma-foot",{attrs:{current:2}})],1)])},staticRenderFns:[]}},411:function(t,r){},499:function(t,r,s){t.exports=s(96)},96:function(t,r,s){"use strict";Object.defineProperty(r,"__esModule",{value:!0});var c=s(1),o=s(269),i=s.n(o),e=s(5),n=(s.n(e),s(3),s(4));c.a.use(n.a),new c.a({el:"#app",render:function(t){return t(i.a)}})}},[499]);