import { ref } from "vue";

export function useProductFilter() {
  const search = ref("");
  const category = ref("all");
  const sort = ref("latest");

  return {
    search,
    category,
    sort,
  };
}
