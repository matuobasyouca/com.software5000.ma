<template>
    <ul :class="simpleWrapClasses" :style="xstyle" v-if="simple">
        <li
                title="上一页"
                :class="prevClasses"
                @click="prev">
            <a><i class="zs-icon zs-icon-arrow-left"></i></a>
        </li>
        <div :class="simplePagerClasses" :title="currentPage + '/' + allPages">
            <input
                    type="number"
                    :value="currentPage"
                    @keydown="keyDown"
                    @keyup="keyUp"
                    @change="keyUp">
            <span>/</span>
            {{ allPages }}
        </div>
        <li
                title="下一页"
                :class="nextClasses"
                @click="next">
            <a><i class="zs-icon zs-icon-arrow-right"></i></a>
        </li>
    </ul>
    <ul :class="wrapClasses" :style="xstyle" v-else>
        <span :class="[prefixCls + '__total']" v-if="showTotal">
            <slot>共 {{ total }} <template v-if="total <= 1">条</template><template v-else>条</template></slot>
        </span>
        <li
                title="上一页"
                :class="prevClasses"
                @click="prev">
            <zs-icon icon="arrow-left" size="10"></zs-icon>
        </li>
        <li title="1" :class="firstPageClasses" @click="changePage(1)"><a>1</a></li>
        <li title="向前5页" v-if="currentPage - 3 > 1" :class="[prefixCls + '__item-jump-prev','zs-icon-']"
            @click="fastPrev">
            ···
        </li>
        <li :title="currentPage - 2" v-if="currentPage - 2 > 1" :class="[prefixCls + '__item']"
            @click="changePage(currentPage - 2)">
            <a>{{ currentPage - 2 }}</a>
        </li>
        <li :title="currentPage - 1" v-if="currentPage - 1 > 1" :class="[prefixCls + '__item']"
            @click="changePage(currentPage - 1)">
            <a>{{ currentPage - 1 }}</a>
        </li>
        <li :title="currentPage" v-if="currentPage != 1 && currentPage != allPages"
            :class="[prefixCls + '__item',prefixCls + '__item-active']">
            <a>{{ currentPage }}</a>
        </li>
        <li :title="currentPage + 1" v-if="currentPage + 1 < allPages" :class="[prefixCls + '__item']"
            @click="changePage(currentPage + 1)">
            <a>{{ currentPage + 1 }}</a>
        </li>
        <li :title="currentPage + 2" v-if="currentPage + 2 < allPages" :class="[prefixCls + '__item']"
            @click="changePage(currentPage + 2)">
            <a>{{ currentPage + 2 }}</a>
        </li>
        <li title="向后5页" v-if="currentPage + 3 < allPages" :class="[prefixCls + '__item-jump-next','zs-icon-']"
            @click="fastNext">
            ···
        </li>
        <li :title="allPages" v-if="allPages > 1" :class="lastPageClasses" @click="changePage(allPages)">
            <a>{{ allPages }}</a>
        </li>
        <li
                title="下一页"
                :class="nextClasses"
                @click="next">
            <zs-icon icon="arrow-right" size="10"></zs-icon>
        </li>
        <span :class="[prefixCls + '__sizer-text']" v-if="showSizer">
            每页显示
        </span>
        <Options
                :show-sizer="showSizer"
                :page-size="currentPageSize"
                :page-size-opts="pageSizeOpts"
                :show-elevator="showElevator"
                :_current.once="currentPage"
                :current="currentPage"
                :all-pages="allPages"
                :is-small="isSmall"
                @on-size="onSize"
                @on-page="onPage">
        </Options>
    </ul>
</template>
<script>
    import Options from './options';

    const prefixCls = 'zs-pagination';

    export default {
        name: 'ZsPagination',
        components: {Options},
        props: {
            current: {
                type: Number,
                default: 1
            },
            total: {
                type: Number,
                default: 0
            },
            pageSize: {
                type: Number,
                default: 10
            },
            pageSizeOpts: {
                type: Array,
                default () {
                    return [10, 20, 30, 40];
                }
            },
            size: {
                validator (value) {
                    return value.indexOf('small') > -1;
                }
            },
            simple: {
                type: Boolean,
                default: false
            },
            showTotal: {
                type: Boolean,
                default: false
            },
            showElevator: {
                type: Boolean,
                default: false
            },
            showSizer: {
                type: Boolean,
                default: false
            },
            className: {
                type: String
            },
            xstyle: {
                type: Object
            }
        },
        data () {
            return {
                prefixCls: prefixCls,
                currentPage: this.current,
                currentPageSize: this.pageSize
            };
        },
        watch: {
            current (val) {
                this.currentPage = val;
            },
            pageSize (val) {
                this.currentPageSize = val;
            }
        },
        computed: {
            isSmall () {
                return !!this.size;
            },
            allPages () {
                const allPage = Math.ceil(this.total / this.currentPageSize);
                return (allPage === 0) ? 1 : allPage;
            },
            simpleWrapClasses () {
                return [
                    `${prefixCls}`,
                    `${prefixCls}__simple`,
                    {
                        [`${this.className}`]: !!this.className
                    }
                ];
            },
            simplePagerClasses () {
                return `${prefixCls}__simple-pager`;
            },
            wrapClasses () {
                return [
                    `${prefixCls}`,
                    {
                        [`${this.className}`]: !!this.className,
                        'mini': !!this.size
                    }
                ];
            },
            prevClasses () {
                return [
                    `${prefixCls}__prev`,
                    {
                        [`${prefixCls}__disabled`]: this.currentPage === 1
                    }
                ];
            },
            nextClasses () {
                return [
                    `${prefixCls}__next`,
                    {
                        [`${prefixCls}__disabled`]: this.currentPage === this.allPages
                    }
                ];
            },
            firstPageClasses () {
                return [
                    `${prefixCls}__item`,
                    {
                        [`${prefixCls}__item-active`]: this.currentPage === 1
                    }
                ];
            },
            lastPageClasses () {
                return [
                    `${prefixCls}__item`,
                    {
                        [`${prefixCls}__item-active`]: this.currentPage === this.allPages
                    }
                ];
            }
        },
        methods: {
            changePage (page) {
                if (this.currentPage != page) {
                    this.currentPage = page;
                    this.$emit('on-change', page);
                }
            },
            prev () {
                const current = this.currentPage;
                if (current <= 1) {
                    return false;
                }
                this.changePage(current - 1);
            },
            next () {
                const current = this.currentPage;
                if (current >= this.allPages) {
                    return false;
                }
                this.changePage(current + 1);
            },
            fastPrev () {
                const page = this.currentPage - 5;
                if (page > 0) {
                    this.changePage(page);
                } else {
                    this.changePage(1);
                }
            },
            fastNext () {
                const page = this.currentPage + 5;
                if (page > this.allPages) {
                    this.changePage(this.allPages);
                } else {
                    this.changePage(page);
                }
            },
            onSize (pageSize) {
                this.currentPageSize = parseInt(pageSize);
                this.changePage(1);
                this.$emit('on-page-size-change', parseInt(pageSize));
            },
            onPage (page) {
                this.changePage(page);
            },
            keyDown (e) {
                const key = e.keyCode;
                const condition = (key >= 33 && key <= 57) || (key >= 96 && key <=105) || key == 8 || key == 12;
                if (!condition) {
                    e.preventDefault();
                }
            },
            keyUp (e) {
                const key = e.keyCode;
                const val = parseInt(e.target.value);

                if (key == 13) {
                    let page = 1;

                    if (val > this.allPages) {
                        page = this.allPages;
                    } else if (val <= 0 || isNaN(val) || val === '') {
                        page = 1;
                    } else {
                        page = val;
                    }
                    e.target.value = page;
                    this.changePage(page);
                }
            }
        }
    };
</script>
