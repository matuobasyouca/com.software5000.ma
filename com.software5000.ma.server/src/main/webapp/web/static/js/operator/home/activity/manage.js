webpackJsonp([8],{207:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=a(315),i=a.n(s),n=a(6),c=a.n(n);t.default={components:{maHead:c.a,list:i.a}}},208:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=a(1),i=a(8);t.default={name:"list",components:{},props:{},data:function(){return{datas:[],context:this,columns:[{title:"活动名称",key:"clusterName",align:"center",width:180},{title:"活动时间",align:"center",width:168,render:function(e){return e.beginDay&&e.endDay?"<p>"+e.beginDay.slice(0,10)+" ~ "+e.endDay.slice(0,10)+"</p>":""}},{title:"拼团商家",key:"businessName",align:"center",width:168},{title:"拼团套餐",key:"packageName",align:"center",width:148},{title:"拼团价格",key:"salePrice",align:"center",width:70},{title:"成团人数",key:"clusterNum",align:"center",width:70},{title:"成团时限",key:"clusterHour",align:"center",width:70},{title:"已成团数",key:"successNum",align:"center",width:70},{title:"活动状态",key:"stateDesc",align:"center",width:70,render:function(e){return 1==e.state?"<p>"+e.stateDesc+" </p>":'<p style="color: #999">'+e.stateDesc+" </p>"}},{title:"操作",key:"followTime",align:"center",className:"account-operation",width:234,render:function(e,t,a,s){return'<a @click="updateState('+e.id+","+e.state+')">{{'+e.state+" == 1 ? '下架' : '上架'}}</a><a @click=\"trunToDetail("+e.id+')">查看</a><a v-if="'+e.canEdit+" != 2 && "+e.state+' != 1" @click="insertActivity('+e.id+')">编辑</a><a v-if="'+e.canEdit+" != 2 && "+e.state+' != 1" @click="showDeleteDialog('+e.id+","+a+')">删除</a><a @click="handleEwmDialog('+e.id+","+a+')">二维码</a>'}}],totalPage:0,search:{keyWord:"",startPage:1,pageSize:10},searchMag:"",ewmDialog:!1,indexMsg:-1,timer:null,ewmState:!1}},computed:{},watch:{ewmDialog:function(e){e||(this.indexMsg=-1)}},methods:{insertActivity:function(e){e?a.i(s.e)("/web/operator/home/activity/add_activity.html",{id:e}):a.i(s.e)("/web/operator/home/activity/add_activity.html")},showDeleteDialog:function(e){var t=this;this.$confirm({type:"delete",title:"删除",showClose:!0,message:"确定删除该拼团？"}).then(function(){t.$ajax(t.$joggle.operator.activity.deletePackCluster,{id:e},!0,function(e,a){a.close(),"ZS011000"===e.code?(t.$message({type:"success",message:e.msg}),1===t.datas.length&&1!=t.search.startPage&&parseFloat(t.search.startPage),t.selectActiveDates()):t.$message({type:"error",message:e.msg})})})},updateState:function(e,t){var a=this;this.$ajax(this.$joggle.operator.activity.updatePackClusterState,{id:e,state:1==t?2:1},!0,function(e,s){s.close(),"ZS011000"===e.code?(a.$message({type:"success",message:1==t?"下架成功":"上架成功",duration:1200}),a.selectActiveDates()):a.$message({type:"error",message:e.msg,duration:1200})})},selectActiveDates:function(){var e=this;this.$ajax(this.$joggle.operator.activity.selectPackClusterByPage,this.search,!0,function(t,a){a.close(),"ZS011000"===t.code?(e.datas=t.data.list,e.totalPage=t.data.total):e.$message({type:"error",message:t.msg,duration:1200})})},handleSearchData:function(){this.search.startPage=1,this.search.keyWord=this.searchMag,this.selectActiveDates()},handleEwmDialog:function(e,t){this.indexMsg=t,this.handleBarCode(e),this.ewmDialog=!0},handleBarCode:function(e){var t=this,n=i.a.ma+"/wap/customer/open/group/activity.html?id="+e+"&t="+(new Date).getTime();a.i(s.i)(i.a.emkt+"/open/coupons/selectQr?type=&url="+n,function(e){t.ewmState=!0,t.$refs.oImg.innerHTML="",t.$refs.oImg.appendChild(e)})},handleClockEwmDialog:function(){this.ewmState=!1,this.ewmDialog=!1},trunToDetail:function(e){a.i(s.e)("/web/operator/home/activity/detail.html",{id:e})},handlePageNum:function(e){this.search.startPage=e,this.selectActiveDates()},handlePageSize:function(e){this.search.startPage=1,this.search.pageSize=e,this.selectActiveDates()}},created:function(){this.selectActiveDates()},mounted:function(){}}},314:function(e,t,a){function s(e){a(475)}var i=a(0)(a(207),a(377),s,null,null);e.exports=i.exports},315:function(e,t,a){function s(e){a(479)}var i=a(0)(a(208),a(381),s,null,null);e.exports=i.exports},377:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"workorder-page"},[a("ma-head",{attrs:{currId:11,operator:!0}}),e._v(" "),a("zs-breadcrumb",[a("zs-breadcrumb-item",[e._v("拼团列表")])],1),e._v(" "),a("list",{tag:"component"})],1)},staticRenderFns:[]}},381:function(e,t,a){e.exports={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"group-buying"},[s("div",{staticClass:"group-header"},[s("zs-button",{attrs:{type:"primary"},on:{click:function(t){e.insertActivity()}}},[e._v("新建拼团")]),e._v(" "),s("div",{staticClass:"r"},[s("zs-input",{attrs:{placeholder:"请输入活动名、商家名称关键词",icon:"circle-cross"},on:{keydown:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.handleSearchData(t)}},model:{value:e.searchMag,callback:function(t){e.searchMag="string"==typeof t?t.trim():t},expression:"searchMag"}}),e._v(" "),s("zs-button",{attrs:{type:"primary"},on:{click:e.handleSearchData}},[s("zs-icon",{attrs:{icon:"search2",size:20}})],1)],1)],1),e._v(" "),s("zs-table",{staticClass:"group-table",attrs:{data:e.datas,columns:e.columns,context:e.context,"no-data-colspan":10,border:"",noDataText:"暂无拼团记录",noDataIcon:"data-empty"}}),e._v(" "),s("div",{staticClass:"page"},[s("zs-pagination",{attrs:{current:e.search.startPage,total:e.totalPage,"page-size":e.search.pageSize,"show-total":"","show-elevator":"","show-sizer":""},on:{"on-change":e.handlePageNum,"on-page-size-change":e.handlePageSize}})],1),e._v(" "),s("zs-dialog",{staticClass:"ewm-dialog",attrs:{"show-close":""},on:{close:e.handleClockEwmDialog},model:{value:e.ewmDialog,callback:function(t){e.ewmDialog=t},expression:"ewmDialog"}},[s("p",{attrs:{slot:"title"},slot:"title"},[e._v("拼团二维码")]),e._v(" "),s("div",{staticClass:"dialog-body"},[s("p",{staticClass:"p1"},[e._v(e._s(-1!=e.indexMsg?e.datas[e.indexMsg].clusterName:""))]),e._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:!e.ewmState,expression:"!ewmState"}],staticClass:"QR-code"},[s("img",{ref:"img",attrs:{src:a(14)}})]),e._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:e.ewmState,expression:"ewmState"}],ref:"oImg",staticClass:"QR-code"})])])],1)},staticRenderFns:[]}},475:function(e,t){},479:function(e,t){},544:function(e,t,a){e.exports=a(71)},71:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=a(2),i=a(314),n=a.n(i),c=a(5),o=(a.n(c),a(3),a(4));s.a.use(o.a),new s.a({el:"#app",render:function(e){return e(n.a)}})}},[544]);