import { watch } from "vue";
import { useToast } from "./useToast";

export function useProductAlert(products, isLoggedIn) {
  const toast = useToast();
  const alerted = new Set();

  watch(
    [products, isLoggedIn],
    ([list, loggedIn]) => {
      if (!loggedIn) return;

      list.forEach((p) => {
        if (p.stock <= 5 && !alerted.has(p.id)) {
          toast.warning(`재고 부족: ${p.name} (${p.stock}개)`);
          alerted.add(p.id);
        }

        if (p.stock > 5 && alerted.has(p.id)) {
          alerted.delete(p.id);
        }
      });
    },
    { deep: true },
  );
}
