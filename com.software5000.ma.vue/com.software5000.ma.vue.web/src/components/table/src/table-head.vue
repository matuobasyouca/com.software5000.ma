<template>
    <thead>
    <tr>
        <th
                v-for="(column, index) in columns"
                :class="alignCls(column)"
                @click="handleSort(index)"
        >
            <div :class="prefixCls + '__cell'">
                <template v-if="column.type === 'selection' && column.title === 'selectAll'">
                    <zs-checkbox :value="isSelectAll" @change="selectAll"></zs-checkbox>
                </template>
                <template v-else>
                    <span>{{ renderHeader(column,index) }}</span>
                    <span :class="[prefixCls + '__sort']" v-if="column.sortable">
                        <i class="sort-caret ascending" :class="{on: column._sortType === 'asc'}"></i>
                        <i class="sort-caret descending" :class="{on: column._sortType === 'desc'}"></i>
                    </span>
                </template>
            </div>
        </th>
    </tr>
    </thead>
</template>
<script>
    import Mixin from './mixin';
    import ZsCheckbox from '../../checkbox';
    import ZsCheckboxGroup from '../../checkbox-group';
    export default {
        name: 'TableHead',
        mixins: [Mixin],
        components: {ZsCheckbox, ZsCheckboxGroup},
        props: {
            prefixCls: String,
            columns: Array,
            data: Array,
            rebuildData: Array
        },

        computed: {
            isSelectAll(){
                let isSelectAll = true;
                let len = this.rebuildData.length;
                if (len === 0) isSelectAll = false;
                for (let i = 0; i < len; i++) {
                    let currRowIndex = this.rebuildData[i]._index;
                    if (!this.data[currRowIndex]._isChecked && !this.data[currRowIndex]._isDisabled) {
                        isSelectAll = false;
                        break;
                    }
                }
                return isSelectAll;
            }
        },

        methods: {
            itemClasses (column, item) {
                return [
                    `${this.prefixCls}__filter-select-item`,
                    {
                        [`${this.prefixCls}__filter-select-item-selected`]: column._filterChecked[0] === item.value
                    }
                ];
            },
            itemAllClasses (column) {
                return [
                    `${this.prefixCls}__filter-select-item`,
                    {
                        [`${this.prefixCls}__filter-select-item-selected`]: !column._filterChecked.length
                    }
                ];
            },
            /**
             * 自定义头部渲染
             * @param column 当前列数据
             * @param index 当前列索引
             * @returns {*}
             */
            renderHeader (column, index) {
                if ('renderHeader' in this.columns[index]) {
                    return this.columns[index].renderHeader(column, index);
                } else {
                    return column.title || '#';
                }
            },

            /**
             * 触发全选
             */
            selectAll(){
                const status = !this.isSelectAll;
                this.$emit('selectAll', status);
            },

            /**
             * 触发排序
             * @param index 排序的列索引
             * @param type 排序的类型
             */
            handleSort (index, type = 'normal') {

                let currCol = this.columns[index];
                if (!currCol.sortable) return;
                let sortTypeMap = ['asc', 'desc', 'normal'];
                if (type === 'normal') {
                    let i = parseInt(currCol._sortTimes % 3)
                    type = sortTypeMap[i];
                }
                if (currCol._sortType === type) {
                    type = 'normal';
                }
                currCol._sortTimes++;
                this.$emit('handleSort', index, type);
            },

            handleFilter (index) {
                this.$emit('handleFilter', index);
            },

            handleSelect (index, value) {
                this.$emit('handleFilterSelect', index, value);
            },
            handleReset (index) {
                this.$emit('handleFilterReset', index);
            }
        }
    };
</script>
