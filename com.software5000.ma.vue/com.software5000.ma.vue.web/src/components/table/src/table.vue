<template>
    <div :class="wrapClasses" :style="wrapStyle">
        <div :class="classes">
            <div :class="[prefixCls+'__title']" v-if="$refs.header">
                <slot name="header"></slot>
            </div>
            <table cellspacing="0" cellpadding="0" border="0">
                <colgroup>
                    <col v-for="(column, index) in columns" :width="column.width ? column.width : ''">
                </colgroup>
                <table-head
                        :prefix-cls="prefixCls"
                        :columns="_columns"
                        :data="objData"
                        :rebuildData="rebuildData"
                        @selectAll="selectAll"
                        @handleSort="handleSort"
                        @handleFilter="handleFilter"
                        @handleSelect="handleFilterSelect"
                        @handleReset="handleFilterReset"
                ></table-head>
                <table-body
                        v-if="rebuildData && rebuildData.length!==0"
                        :prefix-cls="prefixCls"
                        :columns="_columns"
                        :data="objData"
                        :rebuildData="rebuildData"
                        :rowClassName="rowClassName"
                        @handleMouseIn="handleMouseIn"
                        @handleMouseOut="handleMouseOut"
                        @clickCurrentRow="clickCurrentRow"
                ></table-body>
                <tbody v-else>
                <tr>
                    <td class="table-empty-data" :colspan="noDataColspan">
                        <zs-icon :icon="noDataIcon" size="62"></zs-icon>
                        <p v-if="!data || data.length === 0">{{ noDataText }}</p>
                        <p v-else>{{ noFilteredDataText }}</p>
                    </td>
                </tr>
                </tbody>
            </table>
            <div :class="[prefixCls + '__footer']" v-if="$refs.footer" ref="footer">
                <slot name="footer"></slot>
            </div>
        </div>

    </div>
</template>
<script>
    import {deepCopy} from '../../src/utils/util';
    import tableHead from './table-head';
    import tableBody from './table-body';
    const prefixCls = "zs-table";
    let rowKey = 1;
    let columnKey = 1;
    export default {
        name: 'ZsTable',
        components: {tableHead, tableBody},
        props: {
            data: {
                type: Array,
                default(){
                    return [];
                }
            },
            columns: {
                type: Array,
                default(){
                    return [];
                }
            },
            size: String,
            width: [Number, String],
            Height: [Number, String],
            stripe: Boolean,
            border: Boolean,
            showHeader: {
                type: Boolean,
                default: true
            },
            highlightRow: Boolean,
            rowClassName: {
                type: Function,
                default(){
                    return ''
                }
            },
            context: Object,
            noDataIcon: {
                type: String,
                default: 'data-empty'
            },
            noDataText: {
                type: String,
                default: '暂无数据'
            },
            noFilteredDataText: {
                type: String,
                default: '暂无匹配数据'
            },
            noDataColspan: ''
        },
        data() {
            return {
                tableWidth: 0,
                columnsWidth: {},
                prefixCls: prefixCls,
                objData: this.makeObjData(),
                rebuildData: [],
                currentContext: this.context,
            }
        },
        computed: {
            __data(){
                let data = deepCopy(this.data);
                data.forEach((row, index) => {
                    row._index = index;
                    row._rowKey = rowKey++;
                    row._isHover = false;
                    row._isDisabled = row._disabled ? row._disabled : false;
                    row._isChecked = row._checked ? row._checked : false;
                    row._isHighlight = row._highlight ? row._highlight : false;
                })
                return data;
            },
            _columns(){
                let columns = deepCopy(this.columns);
                columns.forEach((column, index) => {
                    column._index = index;
                    column._columnKey = columnKey++;
                    column._width = column.width ? column.width : '';
                    column._sortType = 'normal';
                    column._sortTimes = 0;
                    column._filterVisible = false;
                    column._isFiltered = false;
                    column._filterChecked = [];
                    column._filterMultiple = column.filterMultiple ? column.filterMultiple : false;
                    if (column.filteredValue) {
                        column._filterChecked = column.filteredValue;
                        column._isFiltered = true;
                    }
                })
                return columns;
            },
            wrapClasses () {
                return [
                    `${prefixCls}__wrapper`,
                    {
                        [`${prefixCls}-with-header`]: this.$refs.header,
                        [`${prefixCls}-with-footer`]: this.$refs.footer
                    }
                ];
            },
            classes () {
                return [
                    `${prefixCls}`,
                    {
                        [`${prefixCls}__${this.size}`]: !!this.size,
                        [`${prefixCls}__border`]: this.border,
                        [`${prefixCls}__stripe`]: this.stripe
                    }
                ];
            },
            wrapStyle () {
                let style = {};
                if (this.height) style.height = `${this.height}px`;
                if (this.width) style.width = `${this.width}px`;
                return style;
            },
        },
        watch: {
            __data: {
                handler(){
                    this.rebuildData = this.getDataWithSortAndFilter();
                    this.objData = this.makeObjData();
                },
                deep: true
            },
            _columns: {
                handler(){
                    this.rebuildData = this.getDataWithSortAndFilter();
                },
                deep: true
            }
        },
        methods: {
            makeObjData () {
                let data = [];
                this.data.forEach((row, index) => {
                    const newRow = deepCopy(row);
                    newRow._index = index;
                    newRow._isHover = false;
                    newRow._isDisabled = newRow._disabled ? newRow._disabled : false;
                    newRow._isChecked = newRow._checked ? newRow._checked : false;
                    newRow._isHighlight = newRow._highlight ? newRow._highlight : false;
                    data[index] = newRow;
                });
                return data;
            },
            /**
             * 行的hover
             * @param _index 当前鼠标移入的row的_index属性
             * @returns
             */
            handleMouseIn (_index) {
                let currRow = this.objData[_index];
                if (!currRow._isHover) currRow._isHover = true;
            },

            /**
             * 行的hover
             * @param _index 当前鼠标移出的row的_index属性
             * @returns
             */
            handleMouseOut (_index) {
                let currRow = this.objData[_index];
                currRow._isHover = false;
            },

            /**
             * 行的高亮
             * @param _index 点击的row的_index属性
             * @returns 监听事件可以获取当前切点击的row和上一个高亮的row
             */
            highlightCurrentRow(_index){
                let currRow = this.objData[_index];
                if (!this.highlightRow || currRow._isHighlight) return;

                let oldIndex = -1;
                for (let i in this.__data) {
                    if (this.objData[i]._isHighlight) {
                        oldIndex = parseInt(i);
                        this.objData[i]._isHighlight = false;
                    }
                }
                currRow._isHighlight = true;
                const oldRow = oldIndex < 0 ? null : this.objData[oldIndex];
                this.$emit('on-highlight-change', currRow, oldRow)
            },

            /**
             * 点击row
             * @param _index 点击的row的_index属性
             * @returns 监听事件可以获取当前切点击的row
             */
            clickCurrentRow(_index){
                this.highlightCurrentRow(_index);
                let currRow = this.objData[_index];
                this.$emit('on-row-click', currRow);
            },

            /**
             * 筛选出选中的数据
             * @param
             * @returns 筛选后的数据
             */
            getSelection(){
                let selectionIndexes = [];
                for (let i in this.objData) {
                    let currRow = this.objData[i];
                    if (currRow._isChecked) selectionIndexes.push(parseInt(i));
                }

                return this.data.filter((row, index) => selectionIndexes.indexOf(index) > -1)
            },

            /**
             * 选中切换
             * @param _index 选中的row的_index属性
             * @returns 监听事件可以获取当前切换的row 和 所有选中的row
             */
            toggleSelect (_index) {
                let toggleRow = this.objData[_index];

                const status = !toggleRow._isChecked;

                toggleRow._isChecked = status;

                const selection = this.getSelection();
                this.$emit('on-select', selection, toggleRow, status);
                this.$emit('on-selection-change', selection);
            },

            /**
             * 全选
             * @param status 选中的状态，全选或者全不选
             * @returns 监听事件可以获取当前选中的所有row
             */
            selectAll(status){
                this.rebuildData.forEach((row) => {
                    let currIndex = row._index;
                    if (!this.objData[currIndex]._isDisabled) {
                        this.objData[currIndex]._isChecked = status;
                    }
                })

                const selection = this.getSelection();
                this.$emit('on-selection-change', selection);
            },

            /**
             * 选中筛选项进行筛选
             * @param index 当前列的索引
             * @param value 筛选依据的值
             * @returns
             */
            handleFilterSelect(index, value){
                this._columns[index]._filterChecked = [value];
                this.handleFilter(index);
            },

            /**
             * 重置当前列的筛选
             * @param index 当前列的索引
             * @returns
             */
            handleFilterReset (index) {
                this.cloneColumns[index]._isFiltered = false;
                this.cloneColumns[index]._filterVisible = false;
                this.cloneColumns[index]._filterChecked = [];

                let filterData = this.makeDataWithSort();
                filterData = this.filterOtherData(filterData, index);
                this.rebuildData = filterData;
            },

            /**
             * 关闭筛选菜单
             * @returns
             */
            hideColumnFilter () {
                this._columns.forEach((col) => col._filterVisible = false);
            },

            /**
             * 筛选操作
             * @param index 当前列的索引
             * @returns
             */
            handleFilter(index){
                const column = this._columns[index];
                let filterData = this.getDataWithSort();
                filterData = this.filterOtherData(filterData, index);
                this.rebuildData = this.filterData(filterData, column);
                this._columns[index]._isFiltered = true;
                this._columns[index]._filterVisible = false;
            },

            /**
             * 根据某一列的筛选条件，筛选出行
             * @param data
             * @param column
             * @returns {Array.<T>|*}
             */
            filterData(data, column){
                return data.filter((row) => {
                    let status = !column._filterChecked.length;
                    for (let i = 0, len = column._filterChecked.length; i < len; i++) {
                        status = column.filterMethod(column._filterChecked[i], row)
                        if (status) break;
                    }
                    return status;
                })
            },

            /**
             * 对当前列以外的列的筛选条件进行筛选
             * @param data
             * @param index
             * @returns {*}
             */
            filterOtherData(data, index){
                this._columns.forEach((col, colIndex) => {
                    if (colIndex !== index) {
                        data = this.filterData(data, col);
                    }
                })
                return data;
            },

            /**
             * 筛选所有列
             * @param
             * @returns {*}
             */
            getDataWithFilter(){
                let data = deepCopy(this.__data);
                this._columns.forEach(col => data = this.filterData(data, col))
                return data;
            },

            /**
             * 排序操作，如果不是远程排序，排序类型不是normal，就根据当前列进行排序。
             * @param index
             * @param type
             */
            handleSort(index, type){
                this._columns.forEach((col) => col._sortType = 'normal');
                const key = this._columns[index].key;
                if (this._columns[index].sortable !== 'custom') {
                    if (type === 'normal') {
                        this.rebuildData = this.getDataWithFilter();
                    } else {
                        this.rebuildData = this.sortData(this.rebuildData, type, index);
                    }
                }
                this._columns[index]._sortType = type;
                this.$emit('on-sort-change', {
                    column: JSON.parse(JSON.stringify(this._columns[index])),
                    key: key,
                    order: type
                })
            },

            /**
             * 根据某一列进行排序，如果定义了sortMethod，则按定义的方法排序
             * 否则，直接升序或者降序排列
             * @param data
             * @param type
             * @param index
             */
            sortData(data, type, index){
                let currColumn = this._columns[index];
                const key = currColumn.key;
                data.sort((a, b) => {
                    if (currColumn.sortMethod) {
                        return currColumn.sortMethod(a[key], b[key], type);
                    } else {
                        if (type == 'asc') {
                            return a[key] > b[key] ? 1 : -1;
                        } else {
                            return a[key] < b[key] ? 1 : -1;
                        }
                    }
                })
                return data;
            },

            /**
             * 按照唯一有排序规则的列进行排序
             * @returns {*}
             */
            getDataWithSort(){
                let data = deepCopy(this.__data);
                let sortType = 'normal';
                let sortIndex = -1;
                let isCustom = false;

                for (let i = 0, len = this._columns.length; i < len; i++) {
                    let currColumn = this._columns[i];
                    if (currColumn._sortType !== 'normal') {
                        sortType = currColumn._sortType;
                        sortIndex = i;
                        isCustom = currColumn.sortable === 'custom';
                        break;
                    }
                }
                if (sortType !== 'normal' && !isCustom) data = this.sortData(data, sortType, sortIndex);
                return data;
            },

            /**
             * 初始排序和筛选
             * @returns {*}
             */
            getDataWithSortAndFilter () {
                let data = this.getDataWithSort();
                this._columns.forEach(col => data = this.filterData(data, col));
                return data;
            }
        },
        created(){
            if (!this.context) this.currentContext = this.$parent;
            this.rebuildData = this.getDataWithSortAndFilter();
        }
    };
</script>
