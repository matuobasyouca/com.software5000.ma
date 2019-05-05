<template>
    <div v-if="showSizer || showElevator" :class="optsClasses">
        <div v-if="showSizer" :class="sizerClasses">
            <zs-select v-model="currentPageSize" :size="size" @change="changeSize">
                <zs-option
                        v-for="item in pageSizeOpts"
                        :key="item"
                        :value="item"
                        style="text-align:center;">{{ item }}
                </zs-option>
            </zs-select>
        </div>
        <div v-if="showElevator" :class="ElevatorClasses">
            <span>第</span>
            <input type="number" v-model="inputPage" @keyup.enter="changePage">
            <span>页</span>
        </div>
        <zs-button v-if="showElevator" type="primary" @click="changePage">确定</zs-button>
    </div>
</template>
<script>
    import ZsSelect from '../../select';
    import ZsOption from '../../select/src/option';
    import ZsButton from '../../button';

    const prefixCls = 'zs-pagination';

    function isValueNumber(value){
        return (/^[1-9][0-9]*$/).test(value + '');
    }

    export default {
        name : 'PageOption',
        components : {ZsSelect, ZsOption, ZsButton},
        props : {
            pageSizeOpts : Array,
            showSizer : Boolean,
            showElevator : Boolean,
            current : Number,
            _current : Number,
            pageSize : Number,
            allPages : Number,
            isSmall : Boolean
        },
        data () {
            return {
                currentPageSize : this.pageSize,
                inputPage : this._current
            };
        },
        watch : {
            pageSize (val) {
                this.currentPageSize = val;
            }
        },
        computed : {
            size () {
                return this.isSmall ? 'small' : 'default';
            },
            optsClasses () {
                return [
                    `${prefixCls}__options`
                ];
            },
            sizerClasses () {
                return [
                    `${prefixCls}__options-sizer`
                ];
            },
            ElevatorClasses () {
                return [
                    `${prefixCls}__options-elevator`
                ];
            }
        },
        methods : {
            changeSize () {
                this.$emit('on-size', this.currentPageSize);
            },
            changePage (event) {
                let val = ('' + this.inputPage).trim();
                let page = 0;

                if (isValueNumber(val)) {
                    val = Number(val);
                    if (val != this.current) {
                        const allPages = this.allPages;

                        if (val > allPages) {
                            page = allPages;
                        } else {
                            page = val;
                        }
                    }
                } else {
                    page = 1;
                }

                if (page) {
                    this.$emit('on-page', page);
                    this.inputPage = page;
                }
            }
        }
    };
</script>