/* eslint-disable */
export default {
  name: "Code",
  path: "/code",
  redirect: "/code/code",
  components: {
    content: () => import(/* webpackChunkName: "setting" */ '@/business/components/code/base')
  },
  children: [
    {
      path: "code",
      name: "code",
      component: () => import(/* webpackChunkName: "api" */ "@/business/components/code/home/Code"),
    },
    {
      path: "rule",
      name: "CodeRule",
      component: () => import(/* webpackChunkName: "api" */ '@/business/components/code/home/Rule'),
    },
    {
      path: "result",
      name: "CodeResult",
      component: () => import(/* webpackChunkName: "api" */ "@/business/components/code/home/Result"),
    },
    {
      path: "resultdetails/:id",
      name: "CodeResultDetails",
      component: () => import(/* webpackChunkName: "api" */ "@/business/components/code/home/ResultDetails"),
    },
  ]
}
