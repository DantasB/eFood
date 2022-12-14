/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import RestauranteComponent from '@/entities/restaurante/restaurante.vue';
import RestauranteClass from '@/entities/restaurante/restaurante.component';
import RestauranteService from '@/entities/restaurante/restaurante.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Restaurante Management Component', () => {
    let wrapper: Wrapper<RestauranteClass>;
    let comp: RestauranteClass;
    let restauranteServiceStub: SinonStubbedInstance<RestauranteService>;

    beforeEach(() => {
      restauranteServiceStub = sinon.createStubInstance<RestauranteService>(RestauranteService);
      restauranteServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<RestauranteClass>(RestauranteComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          restauranteService: () => restauranteServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      restauranteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllRestaurantes();
      await comp.$nextTick();

      // THEN
      expect(restauranteServiceStub.retrieve.called).toBeTruthy();
      expect(comp.restaurantes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      restauranteServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeRestaurante();
      await comp.$nextTick();

      // THEN
      expect(restauranteServiceStub.delete.called).toBeTruthy();
      expect(restauranteServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
